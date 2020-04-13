<template>
  <el-container class="home-container">
    <el-header height="80px">
      <h2>飞行数据记录器管理服务</h2>
    </el-header>
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="230px">
        <!-- 菜单 -->
        <el-menu default-active="2" class="el-menu-vertical-demo" background-color="#2d3a4b"
                 text-color="#ffffff" active-text-color="#409bff" :router="true">
          <!-- 实体管理 -->
          <el-submenu index="entity_management">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>实体管理</span>
            </template>
            <!-- 数据点管理 -->
            <el-menu-item index="/home/point">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>数据点管理</span>
              </template>
            </el-menu-item>
            <!-- 过滤器管理 -->
            <el-menu-item index="/home/filter-info">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>过滤器管理</span>
              </template>
            </el-menu-item>
            <!-- 触发器管理 -->
            <el-menu-item index="/home/trigger-info">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>触发器管理</span>
              </template>
            </el-menu-item>
          </el-submenu>
          <!-- 数据支持 -->
          <el-submenu index="data_support">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>数据支持</span>
            </template>
            <!-- 过滤器支持 -->
            <el-menu-item index="/home/filter-support">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>过滤器支持</span>
              </template>
            </el-menu-item>
            <!-- 触发器支持 -->
            <el-menu-item index="/home/trigger-support">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>触发器支持</span>
              </template>
            </el-menu-item>
            <!-- 触发器支持 -->
            <el-menu-item index="/home/mapper-support">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>映射器支持</span>
              </template>
            </el-menu-item>
          </el-submenu>
          <!-- 数据查询 -->
          <el-submenu index="data_inspection">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span slot="title">数据查询</span>
            </template>
            <!-- 实时数据查询 -->
            <el-menu-item index="/home/realtime-value">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>实时数据查询</span>
              </template>
            </el-menu-item>
            <!-- 持久数据查询 -->
            <el-menu-item index="/home/realtime-value">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>持久数据查询</span>
              </template>
            </el-menu-item>
            <!-- 过滤数据查询 -->
            <el-menu-item index="/home/realtime-value">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>过滤数据查询</span>
              </template>
            </el-menu-item>
            <!-- 触发数据查询 -->
            <el-menu-item index="/home/realtime-value">
              <template slot="title">
                <i class="el-icon-s-grid"></i>
                <span>触发数据查询</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Home',
};
</script>

<style type="less" scoped>
  @bg: #2d3a4b;
  @dark_gray: #889aa4;
  @light_gray: #eee;

  .home-container {
    width: 100%;
    height: 100%;
  }

  .el-header {
    display: flex;
    justify-content: flex-start;
    background-color: #1c242d;
    color: #889aa4;
    text-align: center;
  }

  .el-aside {
    background-color: #2d3a4b;
    color: #889aa4;
    line-height: 200px;
    padding: 0;
    margin: 0;
    width: auto;
  }

  .el-main {
    background-color: #eeeeee;
    color: #889aa4;
  }

  .el-menu {
    padding: 0;
    margin: 0;
    border-right: 0;
  }
</style>
