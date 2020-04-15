<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>触发数据查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="7">
            <el-input
              placeholder="请输入数据点ID或触发器ID，仅支持精确查询"
              class="input-with-select"
              v-model="id2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-select v-model="idType" placeholder="请选择" class="search-row">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="12">
            <el-date-picker
              class="search-row"
              v-model="dateRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="timestamp"
            >
            </el-date-picker>
          </el-col>
          <el-col :span="2">
            <el-button
              type="primary"
              icon="el-icon-search"
              class="search-row"
              @click="handleSearch"
            >
              搜索
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="triggeredValue.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="point_key.long_id"
          label="数据点ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="trigger_key.long_id"
          label="触发器ID"
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
        <el-table-column
          prop="message"
          label="信息"
        >
        </el-table-column>
        <el-table-column label="操作" :width="100">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleShowDetailDialog(scope.row)">详细
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
              :total="parseInt(triggeredValue.count)"
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
      :visible.sync="detailVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updatePermissionRules"
        status-icon
        :model="anchorTriggeredValue"
        ref="detailForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorTriggeredValue.key"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorTriggeredValue.point_key"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="触发器ID" prop="triggerKey">
          <el-input
            v-model="anchorTriggeredValue.trigger_key"
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
            v-model="anchorTriggeredValue.value"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="信息" prop="message">
          <el-input
            v-model="anchorTriggeredValue.message"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="this.detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  exists, inspect, all, allBetween, childForPoint, childForPointBetween,
  childForTrigger, childForTriggerBetween,
} from '../../api/triggeredValue';

export default {
  name: 'Permission',
  data() {
    const validateTriggeredValueNotExists = (rule, value, callback) => {
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
      triggeredValue: {},
      pageSize: 15,
      currentPage: 1,
      detailVisible: false,
      anchorTriggeredValue: {
        key: '',
        point_key: '',
        trigger_key: '',
        happened_date: '',
        value: '',
        message: '',
      },
      createPermissionRules: {
        key: [
          {
            validator: validateTriggeredValueNotExists,
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
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一分钟',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一小时',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            },
          },
        ],
      },
      dateRange: null,
      options: [
        {
          value: 'point',
          label: '数据点',
        },
        {
          value: 'trigger',
          label: '触发器',
        },
      ],
      idType: 'point',
    };
  },
  created() {
    this.lookupAll();
  },
  methods: {
    onPageChanged() {
      this.handleSearch();
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupAllBetween() {
      allBetween(this.dateRange[0], this.dateRange[1], this.currentPage - 1, this.pageSize)
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
            return allBetween(this.dateRange[0], this.dateRange[1], res.data.total_pages - 1,
              this.pageSize);
          }
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForPoint() {
      childForPoint(this.id2Search, this.currentPage - 1, this.pageSize)
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
            return childForPoint(this.id2Search, res.data.total_pages - 1, this.pageSize);
          }
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForPointBetween() {
      childForPointBetween(this.id2Search, this.dateRange[0], this.dateRange[1],
        this.currentPage - 1, this.pageSize)
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
            return childForPointBetween(this.id2Search, this.dateRange[0], this.dateRange[1],
              res.data.total_pages - 1, this.pageSize);
          }
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForTrigger() {
      childForTrigger(this.id2Search, this.currentPage - 1, this.pageSize)
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
            return childForTrigger(this.id2Search, res.data.total_pages - 1, this.pageSize);
          }
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForTriggerBetween() {
      childForTriggerBetween(this.id2Search, this.dateRange[0], this.dateRange[1],
        this.currentPage - 1, this.pageSize)
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
            return childForTriggerBetween(this.id2Search, this.dateRange[0], this.dateRange[1],
              res.data.total_pages - 1, this.pageSize);
          }
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          console.log(err);
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
          this.triggeredValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleSearch() {
      if (this.idType === 'point') {
        if (this.id2Search === '' && this.dateRange == null) {
          this.lookupAll();
        } else if (this.id2Search === '' && this.dateRange != null) {
          this.lookupAllBetween();
        } else if (this.id2Search !== '' && this.dateRange == null) {
          this.lookupChildForPoint();
        } else if (this.id2Search !== '' && this.dateRange != null) {
          this.lookupChildForPointBetween();
        }
      } else if (this.id2Search === '' && this.dateRange == null) {
        this.lookupAll();
      } else if (this.id2Search === '' && this.dateRange != null) {
        this.lookupAllBetween();
      } else if (this.id2Search !== '' && this.dateRange == null) {
        this.lookupChildForTrigger();
      } else if (this.id2Search !== '' && this.dateRange != null) {
        this.lookupChildForTriggerBetween();
      }
    },
    handleShowDetailDialog(row) {
      if (this.$refs.detailForm !== undefined) {
        this.$refs.detailForm.resetFields();
      }
      this.anchorTriggeredValue.key = row.key.long_id;
      this.anchorTriggeredValue.point_key = row.point_key.long_id;
      this.anchorTriggeredValue.trigger_key = row.trigger_key.long_id;
      this.anchorTriggeredValue.happened_date = row.happened_date;
      this.anchorTriggeredValue.value = row.value;
      this.anchorTriggeredValue.message = row.message;
      this.detailVisible = true;
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
      if (this.useTimer && this.detailVisible) {
        this.timer = setInterval(() => {
          inspect(this.anchorTriggeredValue.key)
            .then((res) => {
              if (res.meta.code !== 0) {
                return null;
              }
              this.anchorTriggeredValue.key = res.data.key.long_id;
              this.anchorTriggeredValue.happened_date = res.data.happened_date;
              this.anchorTriggeredValue.value = res.data.value;
              return null;
            }).catch(() => null);
        }, 1000);
      } else if (this.timer != null) {
        clearInterval(this.timer);
      }
    },
  },
  computed: {
    formattedDate() {
      const timestamp = this.anchorTriggeredValue.happened_date;
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

  .search-row {
    width: 100%;
  }
</style>
