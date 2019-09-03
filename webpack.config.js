const path = require('path');
const Dotenv = require('dotenv-webpack');
const webpack = require("webpack");

const isProduction = process.env.npm_lifecycle_event === "build";

module.exports = {
    entry: './src/main/typescript/index.tsx',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'target/classes/static')
    },
    module: {
        rules: [
            {
                test: /\.tsx$/,
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
    plugins: isProduction? []:
        [ // not applied in production, as it overrides Heroku
            new Dotenv({
                path: ".env",
                safe: true
            })
        ]
};

