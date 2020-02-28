const path = require('path');
const base = require('./webpack.base.conf');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');

const [cssLoader]  = base.module.rules;
cssLoader.use.push({
  loader: '@alifd/next-theme-loader',
  options: {
    modifyVars: {
      '$icon-font-path':'"/nacos/console-fe/public/icons/icon-font"',
      '$font-custom-path': '"/nacos/console-fe/public/fonts/"'
    }
  }
})
module.exports = Object.assign({}, base, {
  optimization: {
    minimizer: [
      new UglifyJsPlugin({
        cache: true,
        parallel: true,
        sourceMap: true,
      }),
      new OptimizeCSSAssetsPlugin({}),
    ],
  },
  plugins: [
    new CleanWebpackPlugin(path.resolve(__dirname, '../dist'), {
      root: path.resolve(__dirname, '../'),
    }),
    ...base.plugins,
    new MiniCssExtractPlugin({
      filename: './css/[name].css',
      chunkFilename: '[id].css',
    }),
  ],
  mode: 'production',
});
