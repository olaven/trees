const path = require('path');
const Dotenv = require('dotenv-webpack');

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
    plugins: [
        new Dotenv({
            path: ".env",
            safe: true
        })
    ]
};

