module.exports = {
  // 默认情况下，Vue CLI 会假设你的应用是被部署在一个域名的根路径上，例如 https://www.my-app.com/。
  // 如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。
  // 例如，如果你的应用被部署在 https://www.my-app.com/my-app/，则设置 publicPath 为 /my-app/。
  publicPath: '/fdr-manager-ui/',
  // 输出文件目录：在npm run build时，生成文件的目录名称
  outputDir: 'dist',
  // 放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录
  assetsDir: 'asset',
  // 是否在构建生产包时生成 sourceMap 文件，false将提高构建速度
  productionSourceMap: false,
  // 默认情况下，生成的静态资源在它们的文件名中包含了 hash 以便更好的控制缓存，
  // 你可以通过将这个选项设为 false 来关闭文件名哈希。(false的时候就是让原来的文件名不改变)
  filenameHashing: false,
  // 代码保存时进行eslint检测
  lintOnSave: 'error',
  // webpack-dev-server 相关配置
  devServer: {
    // 自动打开浏览器
    open: true,
    // 设置为0.0.0.0则所有的地址均能访问
    host: '0.0.0.0',
    port: 8088,
    https: false,
    hotOnly: false,
  },
};
