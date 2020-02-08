const express = require('express')
const mongoose = require('mongoose')

var app = express()

var Sales = require('./hw2-skeleton/hw2-skeleton/q2schema')

mongoose.connect( "mongodb://localhost:27017/cse", { useNewUrlParser: true, useFindAndModify: false });