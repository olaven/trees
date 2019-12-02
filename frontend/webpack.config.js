const path = require('path');
const Dotenv = require('dotenv-webpack');

module.exports = {
    entry: './src/index.tsx',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'build')
    },
    devServer: {
        contentBase: path.join(__dirname, 'build'),
        port: 8080
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            }
        ]
    },
    resolve: {
        extensions: ['.ts', '.js', '.tsx', 'jsx']
    },
    plugins: [ // not applied in production, as it overrides Heroku
        new Dotenv({
            path: ".env",
            safe: true
        })
    ]
};

