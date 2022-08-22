const express = require('express');
const bodyParser = require("body-parser");
const feedRoute = require('./route/feed');
const cors = require("cors");

const app = express();

const allowedOrigins = ["http://localhost:3000", "http://localhost:8080"];

app.use(
    cors({
        origin: function(origin, callback) {
            if (!origin) return callback(null, true);
            if (allowedOrigins.indexOf(origin) === -1) {
                var msg =
                    "The CORS policy for this site does not " +
                    "allow access from the specified Origin.";
                return callback(new Error(msg), false);
            }
            return callback(null, true);
        }
    })
);

app.use(bodyParser.json()); //application json
app.use('/feed', feedRoute);
app.listen(8080);