/*
 * @Author: deep.ng
 * @Date: 2020-03-12 14:33:52
 */
const path = require('path');
const webpack = require('webpack');
const base = require('./webpack.base.conf');

module.exports = Object.assign({}, base, {
  output: {
    filename: './js/[name].js',
    path: path.resolve(__dirname, '../dist'),
  },
  devServer: {
    port: process.env.PORT || 8000,
    proxy: [{
      context: ['/'],
      changeOrigin: true,
      secure: false,
      target: 'http://localhost:8849',
      pathRewrite: {'^/v1' : '/app/v1'}
    }],
    disableHostCheck: true,
    open: true,
    hot: true,
    overlay: true
  },
  mode: 'development',
  devtool: 'eval-source-map',
  plugins: [
    ...base.plugins,
    new webpack.HotModuleReplacementPlugin()
  ]
});
