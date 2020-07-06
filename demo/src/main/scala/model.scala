import chisel3.{Bits, Bundle, Input, Module, Output, UInt, when}
import chisel3._
import chisel3.util.PriorityEncoder


class model extends Module
{
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val temp_vec = Vec(false.B, false.B,false.B,false.B,false.B,false.B, false.B)
  val  head    = UInt("b11")
  io.out :=AgePriorityEncoder(temp_vec,head)
}
