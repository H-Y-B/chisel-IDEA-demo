import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util.{PopCount, PriorityEncoder, UIntToOH}


class test_model(c:model)extends PeekPokeTester(c)
{
  for(t<- 0 until(100))
  {
    val in1 = rnd.nextInt(100)  //return [0,100)

    //poke(c.io.in1,in1)
    //expect(c.io.out,(1&1)|(0&0))
    step(2)
    //println("in1   "+in1+"      out======" +peek(c.io.out).toString(2))
    //println("in1   "+in1+"      out======" +peek(c.io.out))

  }
}

object test_model
{
  def main(args: Array[String]): Unit =
  {
    //运行测试
    // chisel3.iotesters.Driver(()=>new model)(c=>new test_model(c))

    //生成代码
    val make_verilog  =Array("--target-dir","generated","--compiler","verilog")
    val make_sverilog =Array("--target-dir","generated","--compiler","sverilog")
    val make_low      =Array("--target-dir","generated","--compiler","low")
    chisel3.Driver.execute(make_verilog,()=>new model)

  }
}