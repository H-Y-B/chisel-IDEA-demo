import chisel3._
import chisel3.util.{PriorityEncoder, log2Ceil}
import chisel3.{Bits, Bundle, Input, Module, Output, UInt, when}


/**
  * Object to return the lowest bit position after the head.
  */
object AgePriorityEncoder
{
  def apply(in: Seq[Bool], head: UInt): UInt = {
    val n        = in.size
    val width    = log2Ceil(in.size)
    val n_padded = 1 << width
    val temp_vec = (0 until n_padded).map(i => if (i < n) in(i) && i.U >= head else false.B) ++ in
    val idx      = PriorityEncoder(temp_vec)
    idx(width-1, 0) //discard msb
  }
}