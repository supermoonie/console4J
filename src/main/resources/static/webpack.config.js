const path = require('path');
const htmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

const ENTRY = {
    home: './src/home/index.js',
    index: './src/index/index.js'
}

const entryHtmlPlugins = Object.keys(ENTRY).map(entryName => {
    return new htmlWebpackPlugin({
        template: `./src/${entryName}/index.html`,
        filename: `${entryName}_index.html`,
        chunks: [entryName]
    });
});

let config = {
    plugins: entryHtmlPlugins,
    entry: ENTRY,
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: '[name]_index.js'
    },
    module: {
        rules: [
            {
                test: /Icon.*\.svg$/,
                use: [
                    {
                        loader: 'babel-loader',
                    },
                    {
                        loader: '@svgr/webpack',
                        options: {
                            babel: false,
                            icon: true,
                        },
                    },
                ],
            },
            {
                test: /\.js|jsx$/,
                use: ['babel-loader'],
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                loader: 'file-loader'
            },
            {
                test: /Image.*\.(png|jpe?g|gif|svg)$/,
                use: [{
                    loader: "file-loader",
                    options: {
                        name: 'images/[name].[ext]',
                    }
                }]
            },
        ]
    },
    resolve: {
        extensions: [
            '.js', '.jsx', '.json'
        ],
        alias: {
            '@': path.join(__dirname, './src')
        }
    }
};

if ('production' === process.env.NODE_ENV) {
    config['optimization'] = {
        minimize: true,
        minimizer: [
            new UglifyJSPlugin()
        ]
    };
}

module.exports = config;