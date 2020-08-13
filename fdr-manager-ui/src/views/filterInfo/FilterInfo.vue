<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>实体管理</el-breadcrumb-item>
      <el-breadcrumb-item>过滤器管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="21">
            <el-input
              placeholder="请输入数据点ID，仅支持精确查询"
              class="input-with-select"
              v-model="pointId2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handlePointIdSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-button class="top-el-button" type="primary" @click="handleShowCreateDialog">添加过滤器
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="filterInfo.data"
        stripe
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left">
              <el-form-item label="内容">
                <el-input
                  v-model="props.row.content"
                  type="textarea"
                  autosize
                  :readonly="true"
                ></el-input>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px"
        >
        </el-table-column>
        <el-table-column
          prop="point_key.long_id"
          label="数据点ID"
          width="180px"
        >
        </el-table-column>
        <el-table-column
          prop="enabled"
          label="是否启用"
          width="80px"
          :formatter="enabledFormatter"
        >
        </el-table-column>
        <el-table-column
          prop="type"
          label="类型"
          width="300px">
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
        </el-table-column>
        <el-table-column label="操作" :width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                @click="handleShowUpdateDialog(scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row.key.long_id)">删除
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
              :page-size="filterInfoPageSize"
              :total="parseInt(filterInfo.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentFilterInfoPage"
              @current-change="onFilterInfoPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加过滤器对话框 -->
    <el-dialog
      title="添加过滤器"
      :visible.sync="createFilterVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="createFilterInfoRules"
        status-icon
        :model="anchorFilterInfo"
        ref="createFilterInfoForm"
      >
        <el-form-item label="过滤器ID" prop="key">
          <el-input
            v-model="anchorFilterInfo.key"
            placeholder="请输入过滤器的主键，如不输入则自动生成"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorFilterInfo.pointKey"
            placeholder="请输入数据点ID"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorFilterInfo.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input
            v-model="anchorFilterInfo.type"
            placeholder="请输入过滤器类型"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="handleShowSupportSelector"
            ></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="配置内容" prop="content">
          <el-input
            v-model="anchorFilterInfo.content"
            placeholder="请输入过滤器配置内容"
            type="textarea"
            autosize
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorFilterInfo.remark"
            placeholder="请输入过滤器备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createFilterVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更新过滤器对话框 -->
    <el-dialog
      title="更新过滤器"
      :visible.sync="updateFilterVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updateFilterInfoRules"
        status-icon
        :model="anchorFilterInfo"
        ref="updateFilterInfoForm"
      >
        <el-form-item label="过滤器ID" prop="key">
          <el-input
            v-model="anchorFilterInfo.key"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorFilterInfo.pointKey"
            placeholder="请输入过滤器ID"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorFilterInfo.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input
            v-model="anchorFilterInfo.type"
            placeholder="请输入过滤器类型"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="handleShowSupportSelector"
            ></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="配置内容" prop="content">
          <el-input
            v-model="anchorFilterInfo.content"
            placeholder="请输入过滤器配置内容"
            type="textarea"
            autosize
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorFilterInfo.remark"
            placeholder="请输入过滤器备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFilterVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 过滤器支持选择对话框 -->
    <el-dialog
      title="选择过滤器支持"
      :visible.sync="selectFilterSupportVisible"
      center>
      <!-- 搜索区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="9">
            <el-input
              placeholder="请输入过滤器支持ID，支持任意部位匹配"
              class="input-with-select"
              v-model="filterSupportId2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleFilterSupportIdLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="15">
            <el-input
              placeholder="请输入过滤器支持标签名称，支持任意部位匹配"
              class="input-with-select"
              v-model="filterSupportLabel2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleFilterSupportLabelLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="filterSupport.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.string_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="label"
          label="标签"
          width="200px"
        >
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述"
        >
        </el-table-column>
        <el-table-column label="操作" :width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleSupportSelected(scope.row)">选择
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
              :page-size="filterSupportPageSize"
              :total="parseInt(filterSupport.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentFilterSupportPage"
              @current-change="onFilterSupportPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {
  exists as existsFilterInfo, all as allFilterInfo, childForPoint, insert, remove, update,
} from '@/api/filterInfo';
import {
  all as allFilterSupport,
  idLike as idLikeFilterSupport,
  labelLike as labelLikeFilterSupport,
} from '@/api/filterSupport';
import { exists as existsPoint } from '../../api/point';

export default {
  name: 'Permission',
  data() {
    const validatePointKeyExists = (rule, value, callback) => {
      existsPoint(value)
        .then((res) => {
          if (res.meta.code !== 0) {
            callback(new Error('无法验证数据点是否存在'));
            return null;
          }
          if (!res.data) {
            callback(new Error('数据点不存在'));
            return null;
          }
          callback();
          return null;
        })
        .catch(() => {
          callback(new Error('无法验证数据点是否存在'));
          return null;
        });
    };
    const validateFilterInfoNotExists = (rule, value, callback) => {
      if (value === '') {
        callback();
      } else {
        existsFilterInfo(value)
          .then((res) => {
            if (res.meta.code !== 0) {
              callback(new Error('无法验证过滤器信息是否存在'));
              return null;
            }
            if (res.data) {
              callback(new Error('过滤器信息已经存在'));
              return null;
            }
            callback();
            return null;
          })
          .catch(() => {
            callback(new Error('无法验证过滤器信息是否存在'));
            return null;
          });
      }
    };

    return {
      filterInfo: {},
      filterSupport: {},
      filterInfoPageSize: 15,
      filterSupportPageSize: 6,
      currentFilterInfoPage: 1,
      currentFilterSupportPage: 1,
      createFilterVisible: false,
      updateFilterVisible: false,
      selectFilterSupportVisible: false,
      anchorFilterInfo: {
        key: '',
        pointKey: '',
        enabled: true,
        remark: '',
        content: '',
        type: '',
      },
      createFilterInfoRules: {
        key: [
          {
            validator: validateFilterInfoNotExists,
            trigger: 'blur',
          },
        ],
        pointKey: [
          {
            required: true,
            message: '必须指定过滤器所属的数据点',
            trigger: 'blur',
          },
          { validator: validatePointKeyExists, trigger: 'blur' },
        ],
        type: [
          {
            required: true,
            message: '请输入过滤器的类型',
            trigger: ['change', 'blur'],
          },
        ],
      },
      updateFilterInfoRules: {
        type: [
          {
            required: true,
            message: '请输入过滤器的类型',
            trigger: ['change', 'blur'],
          },
        ],
      },
      pointId2Search: '',
      filterSupportId2Search: '',
      filterSupportLabel2Search: '',
    };
  },
  created() {
    this.lookupAllFilterInfo();
  },
  methods: {
    onFilterInfoPageChanged() {
      this.lookupAllFilterInfo();
    },
    onFilterSupportPageChanged() {
      this.lookupAllFilterSupport();
    },
    lookupAllFilterInfo() {
      allFilterInfo(this.currentFilterInfoPage - 1, this.filterInfoPageSize)
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
            return allFilterInfo(res.data.total_pages - 1, this.filterInfoPageSize);
          }
          this.filterInfo = res.data;
          this.currentFilterInfoPage = res.data.current_page + 1;
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
          this.filterInfo = res.data;
          this.currentFilterInfoPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupAllFilterSupport() {
      allFilterSupport(this.currentFilterSupportPage - 1, this.filterSupportPageSize)
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
            return allFilterSupport(res.data.total_pages - 1, this.filterSupportPageSize);
          }
          this.filterSupport = res.data;
          this.currentFilterSupportPage = res.data.current_page + 1;
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
          this.filterSupport = res.data;
          this.currentFilterSupportPage = res.data.current_page + 1;
          return null;
        });
    },
    handleShowCreateDialog() {
      this.createFilterVisible = true;
    },
    handleShowUpdateDialog(row) {
      if (this.$refs.updateFilterInfoForm !== undefined) {
        this.$refs.updateFilterInfoForm.resetFields();
      }
      this.anchorFilterInfo.key = row.key.long_id;
      this.anchorFilterInfo.pointKey = row.point_key.long_id;
      this.anchorFilterInfo.enabled = row.enabled;
      this.anchorFilterInfo.type = row.type;
      this.anchorFilterInfo.remark = row.remark;
      this.anchorFilterInfo.content = row.content;
      this.updateFilterVisible = true;
    },
    handleCreate() {
      if (this.$refs.createFilterInfoForm === undefined) {
        return;
      }
      this.$refs.createFilterInfoForm.validate((bool) => {
        if (!bool) {
          return;
        }
        insert(
          this.anchorFilterInfo.key,
          this.anchorFilterInfo.pointKey,
          this.anchorFilterInfo.enabled,
          this.anchorFilterInfo.remark,
          this.anchorFilterInfo.content,
          this.anchorFilterInfo.type,
        )
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '创建失败',
                type: 'error',
                center: true,
              });
              return null;
            }
            this.$message({
              showClose: true,
              message: `过滤器 ${res.data.long_id} 创建成功`,
              type: 'success',
              center: true,
            });
            this.createFilterVisible = false;
            this.lookupAllFilterInfo();
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
          });
      });
    },
    handleUpdate() {
      if (this.$refs.updateFilterInfoForm === undefined) {
        return;
      }
      this.$refs.updateFilterInfoForm.validate((bool) => {
        if (!bool) {
          return;
        }
        update(
          this.anchorFilterInfo.key,
          this.anchorFilterInfo.pointKey,
          this.anchorFilterInfo.enabled,
          this.anchorFilterInfo.remark,
          this.anchorFilterInfo.content,
          this.anchorFilterInfo.type,
        )
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '更新失败',
                type: 'error',
                center: true,
              });
              return null;
            }
            this.$message({
              showClose: true,
              message: '过滤器更新成功',
              type: 'success',
              center: true,
            });
            this.updateFilterVisible = false;
            this.lookupAllFilterInfo();
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
          });
      });
    },
    handleDelete(key) {
      this.$confirm('此操作将永久删除此过滤器,同时造成相关过滤数据点位信息丢失。<br>'
          + '<b>我们强烈建议您只是将该过滤器的可用性设置成false，而不是删除。</b><br>'
          + '是否确定要删除该过滤器?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        dangerouslyUseHTMLString: true,
        type: 'warning',
      })
        .then(() => remove(key))
        .catch(() => Promise.reject(new Error('canceled')))
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '删除失败',
              type: 'error',
              center: true,
            });
            return null;
          }
          this.$message({
            type: 'success',
            message: '删除成功!',
          });
          this.lookupAllFilterInfo();
          return null;
        })
        .catch((err) => {
          if (err.message === 'canceled') {
            return null;
          }
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        });
    },
    handlePointIdSearch() {
      if (this.pointId2Search === '') {
        this.lookupAllFilterInfo();
      } else {
        childForPoint(this.pointId2Search, 0, this.filterInfoPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.filterInfo = res.data;
            this.currentFilterInfoPage = res.data.current_page + 1;
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

            this.filterInfo.count = 1;
            this.filterInfo.current_page = 0;
            this.filterInfo.data = [res.data];
            this.filterInfo.rows = this.filterInfoPageSize;
            this.filterInfo.totle_pages = 1;
            this.currentFilterInfoPage = 1;
            return null;
          });
      }
    },
    handleShowSupportSelector() {
      this.lookupAllFilterSupport();
      this.selectFilterSupportVisible = true;
    },
    handleFilterSupportIdLikeSearch() {
      if (this.filterSupportId2Search === '') {
        this.lookupAllFilterSupport();
      } else {
        idLikeFilterSupport(this.filterSupportId2Search, 0, this.filterSupportPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.filterSupport = res.data;
            this.currentFilterSupportPage = res.data.current_page + 1;
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
          });
      }
    },
    handleFilterSupportLabelLikeSearch() {
      if (this.filterSupportLabel2Search === '') {
        this.lookupAllFilterSupport();
      } else {
        labelLikeFilterSupport(this.filterSupportLabel2Search, 0, this.filterSupportPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.filterSupport = res.data;
            this.currentFilterSupportPage = res.data.current_page + 1;
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
          });
      }
    },
    handleSupportSelected(row) {
      this.anchorFilterInfo.type = row.key.string_id;
      this.anchorFilterInfo.content = row.example_content;
      this.selectFilterSupportVisible = false;
    },
    enabledFormatter(row) {
      return row.enabled ? '是' : '否';
    },
  },
};
</script>

<style scoped>
  .pagination-container {
    text-align: center;
  }

  .top-el-button {
    width: 100%;
  }
</style>
