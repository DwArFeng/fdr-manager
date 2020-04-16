<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据支持</el-breadcrumb-item>
      <el-breadcrumb-item>触发器支持</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="9">
            <el-input
              placeholder="请输入触发器支持ID，支持任意部位匹配"
              class="input-with-select"
              v-model="id2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleIdLikeSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="15">
            <el-input
              placeholder="请输入触发器支持标签名称，支持任意部位匹配"
              class="input-with-select"
              v-model="label2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleLabelLikeSearch"
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
          width="400px"
        >
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述"
        >
        </el-table-column>
        <el-table-column
          prop="example_content"
          label="配置示例"
          :formatter="exampleContentFormatter"
          width="100px">
        </el-table-column>
        <el-table-column label="操作" :width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleShowDetailDialog(scope.row)">详细信息
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
              :total="parseInt(triggerSupport.count)"
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
      title="详细信息"
      :visible.sync="showDetailVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        status-icon
        :model="anchorTriggerSupport"
        ref="createPermissionForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorTriggerSupport.key"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="label">
          <el-input
            v-model="anchorTriggerSupport.label"
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="anchorTriggerSupport.description"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="配置示例" prop="exampleContent">
          <el-input
            v-model="anchorTriggerSupport.exampleContent"
            type="textarea"
            autosize
            :readonly="true"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="showDetailVisible = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { all, idLike, labelLike } from '../../api/triggerSupport';

export default {
  name: 'Permission',
  data() {
    return {
      triggerSupport: {},
      pageSize: 15,
      currentPage: 1,
      anchorTriggerSupport: {
        key: '',
        label: '',
        description: '',
        exampleContent: '',
      },
      showDetailVisible: false,
      id2Search: '',
      label2Search: '',
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
          this.triggerSupport = res.data;
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
          this.triggerSupport = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleShowDetailDialog(row) {
      if (this.$refs.updatePermissionForm !== undefined) {
        this.$refs.updatePermissionForm.resetFields();
      }
      this.anchorTriggerSupport.key = row.key.string_id;
      this.anchorTriggerSupport.label = row.label;
      this.anchorTriggerSupport.description = row.description;
      this.anchorTriggerSupport.exampleContent = row.example_content;
      this.showDetailVisible = true;
    },
    exampleContentFormatter() {
      return '见详细页面';
    },
    handleIdLikeSearch() {
      if (this.id2Search === '') {
        this.lookupAll();
      } else {
        idLike(this.id2Search, 0, this.pageSize)
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
          });
      }
    },
    handleLabelLikeSearch() {
      if (this.label2Search === '') {
        this.lookupAll();
      } else {
        labelLike(this.label2Search, 0, this.pageSize)
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
</style>
