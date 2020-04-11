<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>实体管理</el-breadcrumb-item>
      <el-breadcrumb-item>数据点管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="6">
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
          <el-col :span="15">
            <el-input
              placeholder="请输入数据点名称，支持任意部位匹配"
              class="input-with-select"
              v-model="name2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleNameLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-button class="top-el-button" type="primary" @click="handleShowCreateDialog">添加数据点
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="point.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="name"
          label="名称">
        </el-table-column>
        <el-table-column
          prop="realtime_enabled"
          label="记录实时数据"
          :formatter="realtimeEnabledFormatter"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="persistence_enabled"
          label="记录持久数据"
          :formatter="persistenceEnabledFormatter"
          width="120px">
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
              :page-size="pageSize"
              :total="parseInt(point.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加数据点对话框 -->
    <el-dialog
      title="添加数据点"
      :visible.sync="createPointVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="createPermissionRules"
        status-icon
        :model="anchorPoint"
        ref="createPermissionForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorPoint.key"
            placeholder="请输入数据点的主键，如不输入则自动生成"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="anchorPoint.name"
            placeholder="请输入数据点名称"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorPoint.remark"
            placeholder="请输入数据点备注"></el-input>
        </el-form-item>
        <el-form-item label="实时记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorPoint.realtime_enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="持久记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorPoint.persistence_enabled"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createPointVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更新数据点对话框 -->
    <el-dialog
      title="更新数据点"
      :visible.sync="updatePermissionVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updatePermissionRules"
        status-icon
        :model="anchorPoint"
        ref="updatePermissionForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorPoint.key"
            :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="key">
          <el-input
            v-model="anchorPoint.name"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorPoint.remark"
            placeholder="请输入数据点备注"></el-input>
        </el-form-item>
        <el-form-item label="实时记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorPoint.realtime_enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="持久记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorPoint.persistence_enabled"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatePermissionVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  all, exists, insert, inspect, nameLike, remove, update,
} from '../../api/point';

export default {
  name: 'Permission',
  data() {
    const validatePointNotExists = (rule, value, callback) => {
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
      point: {},
      pageSize: 15,
      currentPage: 1,
      createPointVisible: false,
      updatePermissionVisible: false,
      anchorPoint: {
        key: '',
        name: '',
        realtime_enabled: false,
        persistence_enabled: false,
        remark: '',
      },
      createPermissionRules: {
        key: [
          {
            validator: validatePointNotExists,
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
      name2Search: '',
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
          this.point = res.data;
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
          this.point = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleShowCreateDialog() {
      this.createPointVisible = true;
    },
    handleShowUpdateDialog(row) {
      if (this.$refs.updatePermissionForm !== undefined) {
        this.$refs.updatePermissionForm.resetFields();
      }
      console.log(row);
      this.anchorPoint.key = row.key.long_id;
      this.anchorPoint.name = row.name;
      this.anchorPoint.realtime_enabled = row.realtime_enabled;
      this.anchorPoint.persistence_enabled = row.persistence_enabled;
      this.anchorPoint.remark = row.remark;
      this.updatePermissionVisible = true;
    },
    handleCreate() {
      if (this.$refs.createPermissionForm === undefined) {
        return;
      }
      this.$refs.createPermissionForm.validate((bool) => {
        if (!bool) {
          return;
        }
        insert(
          this.anchorPoint.key,
          this.anchorPoint.name,
          this.anchorPoint.remark,
          this.anchorPoint.realtime_enabled,
          this.anchorPoint.persistence_enabled,
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
              message: `数据点 ${res.data.long_id} 创建成功`,
              type: 'success',
              center: true,
            });
            this.createPointVisible = false;
            this.lookupAll();
            return null;
          })
          .catch((err) => {
            console.log(err);
            this.$message({
              showClose: true,
              message: '服务器通信失败',
              type: 'error',
              center: true,
            });
            return null;
          });
      });
    },
    handleUpdate() {
      if (this.$refs.updatePermissionForm === undefined) {
        return;
      }
      this.$refs.updatePermissionForm.validate((bool) => {
        if (!bool) {
          return;
        }
        update(
          this.anchorPoint.key,
          this.anchorPoint.name,
          this.anchorPoint.remark,
          this.anchorPoint.realtime_enabled,
          this.anchorPoint.persistence_enabled,
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
              message: '数据点更新成功',
              type: 'success',
              center: true,
            });
            this.updatePermissionVisible = false;
            this.lookupAll();
            return null;
          })
          .catch((err) => {
            console.log(err);
            this.$message({
              showClose: true,
              message: '服务器通信失败',
              type: 'error',
              center: true,
            });
            return null;
          });
      });
    },
    handleDelete(key) {
      this.$confirm('此操作将永久删除此数据点,同时永久删除属于该数据点的所有配置和历史数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => remove(key))
        .catch(() => {
          console.log('123456789');
          return Promise.reject(new Error('canceled'));
        })
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
          this.lookupAll();
          return null;
        })
        .catch((err) => {
          console.log(err);
          if (err.message === 'canceled') {
            return null;
          }
          console.log(err);
          this.$message({
            showClose: true,
            message: '服务器通信失败',
            type: 'error',
            center: true,
          });
          return null;
        });
    },
    realtimeEnabledFormatter(row) {
      return row.realtime_enabled ? '是' : '否';
    },
    persistenceEnabledFormatter(row) {
      return row.persistence_enabled ? '是' : '否';
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
            console.log(err);
            this.$message({
              showClose: true,
              message: '服务器通信失败',
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

            this.point.count = 1;
            this.point.current_page = 0;
            this.point.data = [res.data];
            this.point.rows = this.pageSize;
            this.point.totle_pages = 1;
            this.currentPage = 1;
            return null;
          });
      }
    },
    handleNameLikeSearch() {
      if (this.name2Search === '') {
        this.lookupAll();
      } else {
        nameLike(this.name2Search, 0, this.pageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.point = res.data;
            this.currentPage = res.data.current_page + 1;
            return null;
          })
          .catch((err) => {
            console.log(err);
            this.$message({
              showClose: true,
              message: '服务器通信失败',
              type: 'error',
              center: true,
            });
            return null;
          });
      }
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
