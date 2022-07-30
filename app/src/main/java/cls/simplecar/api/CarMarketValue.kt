package cls.simplecar.api

data class CarMarketValue constructor(
    val vinCar: String,
    val success: Boolean,
    val retail: Long,
    val tradeIn: Long,
    val roughTradeIn: Long,
    val averageTradeIn: Long,
    val loanValue: Long,
    val msrp: Long
){
    constructor() : this("",false,-1,-1,-1,-1,-1,-1)

}



