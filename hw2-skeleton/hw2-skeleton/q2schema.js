var mongoose = require('mongoose');

var salesShchema = new mongoose.Schema({
    region: String,
    item: String,
    channel: String,
    date: Date,
    units: Number,
    profit: Number
    
});

module.exports = mongoose.model ("Sales", salesShchema);