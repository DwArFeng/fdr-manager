<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>实时数据查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-input
              placeholder="请输入数据点ID，仅支持精确查询"
              class="input-with-select"
              v-model="id2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleIdSearch"></el-button>
            </el-input>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="realtimeValue.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="happened_date"
          label="发生时间"
          :formatter="timestampFormatter"
          width="180px"
        >
        </el-table-column>
        <el-table-column
          prop="value"
          label="数据值"
        >
        </el-table-column>
        <el-table-column label="操作" :width="100">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleShowMonitorDialog(scope.row)">监视
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息区域 -->
      <el-row type="flex" justify="end">
        <el-col>
          <div class="pagination-container">
            <el-pagination
              background
              layout="prev, pager, next"
              :page-size="pageSize"
              :total="parseInt(realtimeValue.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 监视对话框 -->
    <el-dialog
      title="监视"
      :visible.sync="monitorVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updatePermissionRules"
        status-icon
        :model="anchorRealtimeValue"
        ref="monitorForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorRealtimeValue.key"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="发生时间" prop="happenedDate">
          <el-input
            v-model="formattedDate"
            placeholder="请输入数据点备注"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据值" prop="value">
          <el-input
            v-model="anchorRealtimeValue.value"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="定时刷新" prop="refresh">
          <el-switch
            v-model="useTimer"
            active-text="自动刷新"
            inactive-text="不刷新"
            @change="refreshTimer"
            >
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeMonitorDialog">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  all, exists, inspect,
} from '@/api/realtimeValue';

export default {
  name: 'Permission',
  data() {
    const validateRealtimeValueNotExists = (rule, value, callback) => {
      if (value === '') {
        callback();
      } else {
        exists(value)
          .then((res) => {
            if (res.meta.code !== 0) {
              callback(new Error('无法验证数据点是否存在'));
              return null;
            }
            if (res.data) {
              callback(new Error('数据点已经存在'));
              return null;
            }
            callback();
            return null;
          })
          .catch(() => {
            callback(new Error('无法验证数据点是否存在'));
            return null;
          });
      }
    };

    return {
      realtimeValue: {},
      pageSize: 15,
      currentPage: 1,
      monitorVisible: false,
      anchorRealtimeValue: {
        key: '',
        happened_date: '',
        value: '',
      },
      createPermissionRules: {
        key: [
          {
            validator: validateRealtimeValueNotExists,
            trigger: 'blur',
          },
        ],
        name: [
          {
            required: true,
            message: '数据点名称是数据点重要的助记符，请指定有意义的值，以免与其它数据点混淆',
            trigger: 'blur',
          },
        ],
      },
      updatePermissionRules: {
        name: [
          {
            required: true,
            message: '数据点名称是数据点重要的助记符，请指定有意义的值，以免与其它数据点混淆',
            trigger: 'blur',
          },
        ],
      },
      id2Search: '',
      timer: null,
      useTimer: true,
    };
  },
  created() {
    this.lookupAll();
  },
  methods: {
    onPageChanged() {
      this.lookupAll();
    },
    lookupAll() {
      all(this.currentPage - 1, this.pageSize)
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          // 当查询的页数大于总页数，自动查询最后一页。
          if (res.data.current_page >= res.data.total_pages && res.data.total_pages > 0) {
            return all(res.data.total_pages - 1, this.pageSize);
          }
          this.realtimeValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        })
        .then((res) => {
          if (res == null) {
            return null;
          }
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          this.realtimeValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleIdSearch() {
      if (this.id2Search === '') {
        this.lookupAll();
      } else {
        exists(this.id2Search)
          .then((res) => {
            if (res.data) {
              return inspect(this.id2Search);
            }
            this.$message({
              showClose: true,
              message: `数据点 ${this.id2Search} 不存在`,
              type: 'warning',
              center: true,
            });
            return null;
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: `通信错误，异常信息: ${err.message}`,
              type: 'error',
              center: true,
            });
            return null;
          })
          .then((res) => {
            if (res == null) {
              return null;
            }
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }

            this.realtimeValue.count = 1;
            this.realtimeValue.current_page = 0;
            this.realtimeValue.data = [res.data];
            this.realtimeValue.rows = this.pageSize;
            this.realtimeValue.totle_pages = 1;
            this.currentPage = 1;
            return null;
          });
      }
    },
    handleShowMonitorDialog(row) {
      if (this.$refs.monitorForm !== undefined) {
        this.$refs.monitorForm.resetFields();
      }
      this.anchorRealtimeValue.key = row.key.long_id;
      this.anchorRealtimeValue.happened_date = row.happened_date;
      this.anchorRealtimeValue.value = row.value;
      this.monitorVisible = true;
      this.refreshTimer();
    },
    timestampFormatter(row) {
      const timestamp = row.happened_date;
      // 时间戳为10位需*1000，时间戳为13位的话不需乘1000 var date = new Date(timestamp*1000);
      const date = new Date(timestamp);
      const Y = `${date.getFullYear()}-`;
      const M = `${date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-`;
      const D = `${date.getDate()} `;
      const h = `${date.getHours()}:`;
      const m = `${date.getMinutes()}:`;
      const s = date.getSeconds();
      return Y + M + D + h + m + s;
    },
    refreshTimer() {
      if (this.useTimer && this.monitorVisible) {
        this.timer = setInterval(() => {
          inspect(this.anchorRealtimeValue.key)
            .then((res) => {
              if (res.meta.code !== 0) {
                return null;
              }
              this.anchorRealtimeValue.key = res.data.key.long_id;
              this.anchorRealtimeValue.happened_date = res.data.happened_date;
              this.anchorRealtimeValue.value = res.data.value;
              return null;
            }).catch(() => null);
        }, 1000);
      } else if (this.timer != null) {
        clearInterval(this.timer);
      }
    },
    closeMonitorDialog() {
      this.monitorVisible = false;
      this.refreshTimer();
    },
  },
  computed: {
    formattedDate() {
      const timestamp = this.anchorRealtimeValue.happened_date;
      // 时间戳为10位需*1000，时间戳为13位的话不需乘1000 var date = new Date(timestamp*1000);
      const date = new Date(timestamp);
      const Y = `${date.getFullYear()}-`;
      const M = `${date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-`;
      const D = `${date.getDate()} `;
      const h = `${date.getHours()}:`;
      const m = `${date.getMinutes()}:`;
      const s = date.getSeconds();
      return Y + M + D + h + m + s;
    },
  },
};
</script>

<style scoped>
  .pagination-container {
    text-align: center;
  }
</style>
