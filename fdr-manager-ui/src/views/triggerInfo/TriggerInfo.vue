<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>实体管理</el-breadcrumb-item>
      <el-breadcrumb-item>触发器管理</el-breadcrumb-item>
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
            <el-button class="top-el-button" type="primary" @click="handleShowCreateDialog">添加触发器
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="triggerInfo.data"
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
              :page-size="triggerInfoPageSize"
              :total="parseInt(triggerInfo.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentTriggerInfoPage"
              @current-change="onTriggerInfoPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加触发器对话框 -->
    <el-dialog
      title="添加触发器"
      :visible.sync="createTriggerVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="createTriggerInfoRules"
        status-icon
        :model="anchorTriggerInfo"
        ref="createTriggerInfoForm"
      >
        <el-form-item label="触发器ID" prop="key">
          <el-input
            v-model="anchorTriggerInfo.key"
            placeholder="请输入触发器的主键，如不输入则自动生成"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorTriggerInfo.pointKey"
            placeholder="请输入数据点ID"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorTriggerInfo.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input
            v-model="anchorTriggerInfo.type"
            placeholder="请输入触发器类型"
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
            v-model="anchorTriggerInfo.content"
            placeholder="请输入触发器配置内容"
            type="textarea"
            autosize
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorTriggerInfo.remark"
            placeholder="请输入触发器备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createTriggerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更新触发器对话框 -->
    <el-dialog
      title="更新触发器"
      :visible.sync="updateTriggerVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updateTriggerInfoRules"
        status-icon
        :model="anchorTriggerInfo"
        ref="updateTriggerInfoForm"
      >
        <el-form-item label="触发器ID" prop="key">
          <el-input
            v-model="anchorTriggerInfo.key"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点ID" prop="pointKey">
          <el-input
            v-model="anchorTriggerInfo.pointKey"
            placeholder="请输入触发器ID"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorTriggerInfo.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input
            v-model="anchorTriggerInfo.type"
            placeholder="请输入触发器类型"
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
            v-model="anchorTriggerInfo.content"
            placeholder="请输入触发器配置内容"
            type="textarea"
            autosize
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorTriggerInfo.remark"
            placeholder="请输入触发器备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateTriggerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 触发器支持选择对话框 -->
    <el-dialog
      title="选择触发器支持"
      :visible.sync="selectTriggerSupportVisible"
      center>
      <!-- 搜索区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="9">
            <el-input
              placeholder="请输入触发器支持ID，支持任意部位匹配"
              class="input-with-select"
              v-model="triggerSupportId2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleTriggerSupportIdLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="15">
            <el-input
              placeholder="请输入触发器支持标签名称，支持任意部位匹配"
              class="input-with-select"
              v-model="triggerSupportLabel2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleTriggerSupportLabelLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="triggerSupport.data"
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
              :page-size="triggerSupportPageSize"
              :total="parseInt(triggerSupport.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentTriggerSupportPage"
              @current-change="onTriggerSupportPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {
  exists as existsTriggerInfo, all as allTriggerInfo, childForPoint, insert, remove, update,
} from '../../api/triggerInfo';
import { exists as existsPoint } from '../../api/point';
import {
  all as allTriggerSupport,
  idLike as idLikeTriggerSupport,
  labelLike as labelLikeTriggerSupport,
} from '../../api/triggerSupport';

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
    const validateTriggerInfoNotExists = (rule, value, callback) => {
      if (value === '') {
        callback();
      } else {
        existsTriggerInfo(value)
          .then((res) => {
            if (res.meta.code !== 0) {
              callback(new Error('无法验证触发器信息是否存在'));
              return null;
            }
            if (res.data) {
              callback(new Error('触发器信息已经存在'));
              return null;
            }
            callback();
            return null;
          })
          .catch(() => {
            callback(new Error('无法验证触发器信息是否存在'));
            return null;
          });
      }
    };

    return {
      triggerInfo: {},
      triggerSupport: {},
      triggerInfoPageSize: 15,
      triggerSupportPageSize: 6,
      currentTriggerInfoPage: 1,
      currentTriggerSupportPage: 1,
      createTriggerVisible: false,
      updateTriggerVisible: false,
      selectTriggerSupportVisible: false,
      anchorTriggerInfo: {
        key: '',
        pointKey: '',
        enabled: true,
        remark: '',
        content: '',
        type: '',
      },
      createTriggerInfoRules: {
        key: [
          {
            validator: validateTriggerInfoNotExists,
            trigger: 'blur',
          },
        ],
        pointKey: [
          {
            required: true,
            message: '必须指定触发器所属的数据点',
            trigger: 'blur',
          },
          { validator: validatePointKeyExists, trigger: 'blur' },
        ],
        type: [
          {
            required: true,
            message: '请输入触发器的类型',
            trigger: ['change', 'blur'],
          },
        ],
      },
      updateTriggerInfoRules: {
        type: [
          {
            required: true,
            message: '请输入触发器的类型',
            trigger: ['change', 'blur'],
          },
        ],
      },
      pointId2Search: '',
      triggerSupportId2Search: '',
      triggerSupportLabel2Search: '',
    };
  },
  created() {
    this.lookupAllTriggerInfo();
  },
  methods: {
    onTriggerInfoPageChanged() {
      this.lookupAllTriggerInfo();
    },
    onTriggerSupportPageChanged() {
      this.lookupAllTriggerSupport();
    },
    lookupAllTriggerInfo() {
      allTriggerInfo(this.currentTriggerInfoPage - 1, this.triggerInfoPageSize)
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
            return allTriggerInfo(res.data.total_pages - 1, this.triggerInfoPageSize);
          }
          this.triggerInfo = res.data;
          this.currentTriggerInfoPage = res.data.current_page + 1;
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
          this.triggerInfo = res.data;
          this.currentTriggerInfoPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupAllTriggerSupport() {
      allTriggerSupport(this.currentTriggerSupportPage - 1, this.triggerSupportPageSize)
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
            return allTriggerSupport(res.data.total_pages - 1, this.triggerSupportPageSize);
          }
          this.triggerSupport = res.data;
          this.currentTriggerSupportPage = res.data.current_page + 1;
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
          this.triggerSupport = res.data;
          this.currentTriggerSupportPage = res.data.current_page + 1;
          return null;
        });
    },
    handleShowCreateDialog() {
      this.createTriggerVisible = true;
    },
    handleShowUpdateDialog(row) {
      if (this.$refs.updateTriggerInfoForm !== undefined) {
        this.$refs.updateTriggerInfoForm.resetFields();
      }
      this.anchorTriggerInfo.key = row.key.long_id;
      this.anchorTriggerInfo.pointKey = row.point_key.long_id;
      this.anchorTriggerInfo.enabled = row.enabled;
      this.anchorTriggerInfo.type = row.type;
      this.anchorTriggerInfo.remark = row.remark;
      this.anchorTriggerInfo.content = row.content;
      this.updateTriggerVisible = true;
    },
    handleCreate() {
      if (this.$refs.createTriggerInfoForm === undefined) {
        return;
      }
      this.$refs.createTriggerInfoForm.validate((bool) => {
        if (!bool) {
          return;
        }
        insert(
          this.anchorTriggerInfo.key,
          this.anchorTriggerInfo.pointKey,
          this.anchorTriggerInfo.enabled,
          this.anchorTriggerInfo.remark,
          this.anchorTriggerInfo.content,
          this.anchorTriggerInfo.type,
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
              message: `触发器 ${res.data.long_id} 创建成功`,
              type: 'success',
              center: true,
            });
            this.createTriggerVisible = false;
            this.lookupAllTriggerInfo();
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
      if (this.$refs.updateTriggerInfoForm === undefined) {
        return;
      }
      this.$refs.updateTriggerInfoForm.validate((bool) => {
        if (!bool) {
          return;
        }
        update(
          this.anchorTriggerInfo.key,
          this.anchorTriggerInfo.pointKey,
          this.anchorTriggerInfo.enabled,
          this.anchorTriggerInfo.remark,
          this.anchorTriggerInfo.content,
          this.anchorTriggerInfo.type,
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
              message: '触发器更新成功',
              type: 'success',
              center: true,
            });
            this.updateTriggerVisible = false;
            this.lookupAllTriggerInfo();
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
      this.$confirm('此操作将永久删除此触发器,同时造成相关触发数据点位信息丢失。<br>'
          + '<b>我们强烈建议您只是将该触发器的可用性设置成false，而不是删除。</b><br>'
          + '是否确定要删除该触发器?', '提示', {
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
          this.lookupAllTriggerInfo();
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
        this.lookupAllTriggerInfo();
      } else {
        childForPoint(this.pointId2Search, 0, this.triggerInfoPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.triggerInfo = res.data;
            this.currentTriggerInfoPage = res.data.current_page + 1;
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

            this.triggerInfo.count = 1;
            this.triggerInfo.current_page = 0;
            this.triggerInfo.data = [res.data];
            this.triggerInfo.rows = this.triggerInfoPageSize;
            this.triggerInfo.totle_pages = 1;
            this.currentTriggerInfoPage = 1;
            return null;
          });
      }
    },
    handleShowSupportSelector() {
      this.lookupAllTriggerSupport();
      this.selectTriggerSupportVisible = true;
    },
    handleTriggerSupportIdLikeSearch() {
      if (this.triggerSupportId2Search === '') {
        this.lookupAllTriggerSupport();
      } else {
        idLikeTriggerSupport(this.triggerSupportId2Search, 0, this.triggerSupportPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.triggerSupport = res.data;
            this.currentTriggerSupportPage = res.data.current_page + 1;
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
    handleTriggerSupportLabelLikeSearch() {
      if (this.triggerSupportLabel2Search === '') {
        this.lookupAllTriggerSupport();
      } else {
        labelLikeTriggerSupport(this.triggerSupportLabel2Search, 0, this.triggerSupportPageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.triggerSupport = res.data;
            this.currentTriggerSupportPage = res.data.current_page + 1;
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
      this.anchorTriggerInfo.type = row.key.string_id;
      this.anchorTriggerInfo.content = row.example_content;
      this.selectTriggerSupportVisible = false;
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
