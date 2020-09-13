package com.bitcoin.ticker.data.model

data class Payload(
    val bits: String,
    val chainwork: String,
    val confirmations: Int,
    val datetime: String,
    val difficulty: Double,
    val hash: String,
    val height: Int,
    val mediantime: String,
    val merkleroot: String,
    val nextblockhash: String,
    val nonce: Int,
    val previousblockhash: String,
    val size: Int,
    val strippedsize: Int,
    val time: String,
    val timestamp: Int,
    val transactions: Int,
    val tx: List<String>,
    val version: Int,
    val versionHex: String,
    val weight: Int
)