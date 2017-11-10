'use strict';

var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var ProvidePlugin = require('webpack').ProvidePlugin;
var path = require('path');
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';

module.exports = (function makeWebpackConfig () {
  var config = {};

  config.entry = {
    app: './src/client/app/app.js'
  };

  config.output = {
    // path: path.resolve(__dirname, './dist'),
    path: __dirname + '/public',
    publicPath: isProd ? '/' : 'http://localhost:8080/',
    filename: isProd ? '[name].[hash].js' : '[name].bundle.js',
    chunkFilename: isProd ? '[name].[hash].js' : '[name].bundle.js'
  };

  if (isProd) {
    config.devtool = 'source-map';
  } else {
    config.devtool = 'eval-source-map';
  }

  config.resolve = {
    modulesDirectories: [
      'node_modules',
      // 'src/client/public'
    ]
  };

  config.module = {
    preLoaders: [],
    loaders: [{
      test: /\.js$/,
      loaders: ['ng-annotate', 'babel-loader'],
      exclude: /node_modules/
    }, {
      test: /\.scss$/,
      loaders: ['style', 'css', 'sass']
    }, {
      test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$/,
      loader: 'file'
    }, {
      test: /\.html$/,
      loader: 'html-loader',
      // loader: 'raw-loader',
      // exclude: /index\.html/
    }]
  };

  config.plugins = [
    new HtmlWebpackPlugin({
      template: './src/client/index.html',
      inject: 'body'
    }),
    new CopyWebpackPlugin([{
      from: 'src/client/app/assets',
      to: 'assets'
    }]),
    new ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery',
      'windows.jQuery': 'jquery',
    })
  ];

  //fix building for PROD!!
  if (isProd) {
    config.plugins.push(
      new webpack.NoErrorsPlugin(),
      new webpack.optimize.DedupePlugin(),
      new webpack.optimize.UglifyJsPlugin()
      // new CopyWebpackPlugin([{
      //   from: path.resolve(__dirname, './src/client/public')
      // }], { ignore: ['*.html'] })
    );
  }

  config.devServer = {
    contentBase: './public',
    stats: 'minimal'
  };

  return config;
}());

