<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>持久数据查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-input
              placeholder="请输入数据点ID，仅支持精确查询"
              class="input-with-select"
              v-model="pointId2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
            </el-input>
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
        :data="persistenceValue.data"
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
              :total="parseInt(persistenceValue.count)"
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
        :model="anchorPersistenceValue"
        ref="detailForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorPersistenceValue.key"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorPersistenceValue.point_key"
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
            v-model="anchorPersistenceValue.value"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  exists, inspect, all, allBetween, childForPoint, childForPointBetween,
} from '@/api/persistenceValue';

export default {
  name: 'Permission',
  data() {
    const validatePersistenceValueNotExists = (rule, value, callback) => {
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
      persistenceValue: {},
      pageSize: 15,
      currentPage: 1,
      detailVisible: false,
      anchorPersistenceValue: {
        key: '',
        point_key: '',
        happened_date: '',
        value: '',
      },
      createPermissionRules: {
        key: [
          {
            validator: validatePersistenceValueNotExists,
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
      pointId2Search: '',
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
          this.persistenceValue = res.data;
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
          this.persistenceValue = res.data;
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
          this.persistenceValue = res.data;
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
          this.persistenceValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForPoint() {
      childForPoint(this.pointId2Search, this.currentPage - 1, this.pageSize)
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
            return childForPoint(this.pointId2Search, res.data.total_pages - 1, this.pageSize);
          }
          this.persistenceValue = res.data;
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
          this.persistenceValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForPointBetween() {
      childForPointBetween(this.pointId2Search, this.dateRange[0], this.dateRange[1],
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
            return childForPointBetween(this.pointId2Search, this.dateRange[0], this.dateRange[1],
              res.data.total_pages - 1, this.pageSize);
          }
          this.persistenceValue = res.data;
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
          this.persistenceValue = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleSearch() {
      if (this.pointId2Search === '' && this.dateRange == null) {
        this.lookupAll();
      } else if (this.pointId2Search === '' && this.dateRange != null) {
        this.lookupAllBetween();
      } else if (this.pointId2Search !== '' && this.dateRange == null) {
        this.lookupChildForPoint();
      } else if (this.pointId2Search !== '' && this.dateRange != null) {
        this.lookupChildForPointBetween();
      }
    },
    handleShowDetailDialog(row) {
      if (this.$refs.detailForm !== undefined) {
        this.$refs.detailForm.resetFields();
      }
      this.anchorPersistenceValue.key = row.key.long_id;
      this.anchorPersistenceValue.point_key = row.point_key.long_id;
      this.anchorPersistenceValue.happened_date = row.happened_date;
      this.anchorPersistenceValue.value = row.value;
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
          inspect(this.anchorPersistenceValue.key)
            .then((res) => {
              if (res.meta.code !== 0) {
                return null;
              }
              this.anchorPersistenceValue.key = res.data.key.long_id;
              this.anchorPersistenceValue.happened_date = res.data.happened_date;
              this.anchorPersistenceValue.value = res.data.value;
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
      const timestamp = this.anchorPersistenceValue.happened_date;
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

  .search-row{
    width: 100%;
  }
</style>
