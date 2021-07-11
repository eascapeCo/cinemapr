module.exports = {
  runtimeCompiler: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'https://localhost:8443',
        secure: false,
        changeOrigin: true,
        publicPath: {
          '^/api': '/'
        }
      }
    }
  },
  outputDir: '../src/main/resources/static',
  indexPath: '../static/index.html',
  //assetsDir: '../static'
  transpileDependencies: [
    'vuetify'
  ]
}
