# ElasticReg

```scala
import boom.util.ElasticReg
import chisel3.{Bits, Bundle, Input, Module, Output, UInt, when}
import chisel3.util.{Enum, is, switch}
import chisel3._
import chisel3.core.withReset

class model extends Module
{
  val io=IO(new Bundle{
      val i_reset=Input(Bool())

      val i_valid=Input(Bool())
      val i_data =Input(UInt(5.W))
      val i_ready=Output(Bool())


      val o_ready=Input(Bool())
      val o_data =Output(UInt(5.W))
      val o_valid=Output(Bool())
  })

  val q_f3_history = withReset(io.i_reset)
  { Module(new ElasticReg(UInt(5.W))) }

  q_f3_history.io.enq.valid := io.i_valid
  q_f3_history.io.enq.bits  := io.i_data
  io.i_ready:=q_f3_history.io.enq.ready

  q_f3_history.io.deq.ready := io.o_ready
  io.o_data:=q_f3_history.io.deq.bits
  io.o_valid:=q_f3_history.io.deq.valid
}

```



```scala
class ElasticReg[T <: Data](gen: T) extends Module
{
  val entries = 2
  val io = IO(new QueueIO(gen, entries) {})

  private val valid = RegInit(VecInit(Seq.fill(entries) { false.B }))
  private val elts  = Reg(Vec(entries, gen))

  for (i <- 0 until entries) {
    def paddedValid(i: Int) = if(i == -1) 
                                  true.B 
                              else if (i == entries) 
                                  false.B 
                              else 
                                  valid(i)

    val wdata = if (i == entries-1) 
                     io.enq.bits 
                else 
                     Mux(valid(i+1), elts(i+1), io.enq.bits)
      
    val wen = Mux(io.deq.ready,
                  paddedValid(i+1) || io.enq.fire() && ((i == 0).B || valid(i)),
                  io.enq.fire() && paddedValid(i-1) && !valid(i)
                  )
      
    when (wen) { elts(i) := wdata }

    valid(i) := Mux(io.deq.ready,
                    paddedValid(i+1) || io.enq.fire() && ((i == 0).B || valid(i)),
                    io.enq.fire() && paddedValid(i-1) || valid(i))
  }

  io.enq.ready := !valid(entries-1)
  io.deq.valid := valid(0)
  io.deq.bits  := elts.head

  io.count := PopCount(valid.asUInt)
}
```



```verilog
module ElasticReg( // @[:@3.2]
  input        clock, // @[:@4.4]
  input        reset, // @[:@5.4]
  output       io_enq_ready, // @[:@6.4]
  input        io_enq_valid, // @[:@6.4]
  input  [4:0] io_enq_bits, // @[:@6.4]
  input        io_deq_ready, // @[:@6.4]
  output       io_deq_valid, // @[:@6.4]
  output [4:0] io_deq_bits // @[:@6.4]
);
  reg  valid_0; // @[ElasticReg.scala 22:30:@11.4]
  //reg [31:0] _RAND_0;
  reg  valid_1; // @[ElasticReg.scala 22:30:@11.4]
  //reg [31:0] _RAND_1;
  reg [4:0] elts_0; // @[ElasticReg.scala 23:25:@12.4]
  //reg [31:0] _RAND_2;
  reg [4:0] elts_1; // @[ElasticReg.scala 23:25:@12.4]
  //reg [31:0] _RAND_3;
  wire [4:0] _T_67; // @[ElasticReg.scala 28:57:@13.4]
  wire  _T_68; // @[Decoupled.scala 37:37:@14.4]
  wire  _T_72; // @[ElasticReg.scala 30:24:@17.4]
  wire  _T_77; // @[ElasticReg.scala 31:44:@20.4]
  wire  _T_78; // @[ElasticReg.scala 31:41:@21.4]
  wire  _T_79; // @[ElasticReg.scala 29:18:@22.4]
  wire  _T_88; // @[ElasticReg.scala 36:41:@32.4]
  wire  _T_89; // @[ElasticReg.scala 34:20:@33.4]
  wire  _T_94; // @[ElasticReg.scala 30:41:@37.4]
  wire  _T_97; // @[ElasticReg.scala 31:21:@40.4]
  wire  _T_99; // @[ElasticReg.scala 31:44:@41.4]
  wire  _T_100; // @[ElasticReg.scala 31:41:@42.4]
  wire  _T_101; // @[ElasticReg.scala 29:18:@43.4]
  wire  _T_110; // @[ElasticReg.scala 36:41:@53.4]
  wire  _T_111; // @[ElasticReg.scala 34:20:@54.4]
    
  assign _T_67 = valid_1 ? elts_1 : io_enq_bits; // @[ElasticReg.scala 28:57:@13.4]
  assign _T_68 = io_enq_ready & io_enq_valid; // @[Decoupled.scala 37:37:@14.4]
  assign _T_72 = valid_1 | _T_68; // @[ElasticReg.scala 30:24:@17.4]
  assign _T_77 = valid_0 == 1'h0; // @[ElasticReg.scala 31:44:@20.4]
  assign _T_78 = _T_68 & _T_77; // @[ElasticReg.scala 31:41:@21.4]
  assign _T_79 = io_deq_ready ? _T_72 : _T_78; // @[ElasticReg.scala 29:18:@22.4]
  assign _T_88 = _T_68 | valid_0; // @[ElasticReg.scala 36:41:@32.4]
  assign _T_89 = io_deq_ready ? _T_72 : _T_88; // @[ElasticReg.scala 34:20:@33.4]
  assign _T_94 = _T_68 & valid_1; // @[ElasticReg.scala 30:41:@37.4]
  assign _T_97 = _T_68 & valid_0; // @[ElasticReg.scala 31:21:@40.4]
  assign _T_99 = valid_1 == 1'h0; // @[ElasticReg.scala 31:44:@41.4]
  assign _T_100 = _T_97 & _T_99; // @[ElasticReg.scala 31:41:@42.4]
  assign _T_101 = io_deq_ready ? _T_94 : _T_100; // @[ElasticReg.scala 29:18:@43.4]
  assign _T_110 = _T_97 | valid_1; // @[ElasticReg.scala 36:41:@53.4]
  assign _T_111 = io_deq_ready ? _T_94 : _T_110; // @[ElasticReg.scala 34:20:@54.4]
  assign io_enq_ready = valid_1 == 1'h0; // @[ElasticReg.scala 39:16:@57.4]
  assign io_deq_valid = valid_0; // @[ElasticReg.scala 40:16:@58.4]
  assign io_deq_bits = elts_0; // @[ElasticReg.scala 41:15:@59.4]

  always @(posedge clock) begin
      
    if (reset) begin
      valid_0 <= 1'h0;
    end else begin
      if (io_deq_ready) begin
        valid_0 <= _T_72;
      end else begin
        valid_0 <= _T_88;
      end
    end
      
    if (reset) begin
      valid_1 <= 1'h0;
    end else begin
      if (io_deq_ready) begin
        valid_1 <= _T_94;
      end else begin
        valid_1 <= _T_110;
      end
    end
      
    if (_T_79) begin
      if (valid_1) begin
        elts_0 <= elts_1;
      end else begin
        elts_0 <= io_enq_bits;
      end
    end
      
    if (_T_101) begin
      elts_1 <= io_enq_bits;
    end
      
  end//end always posedge clock
    
    
endmodule
```



```verilog
module model( // @[:@66.2]
  input        clock, // @[:@67.4]
  input        reset, // @[:@68.4]
  input        io_i_reset, // @[:@69.4]
  input        io_i_valid, // @[:@69.4]
  input  [4:0] io_i_data, // @[:@69.4]
  output       io_i_ready, // @[:@69.4]
  input        io_o_ready, // @[:@69.4]
  output [4:0] io_o_data, // @[:@69.4]
  output       io_o_valid // @[:@69.4]
);
  wire  q_f3_history_clock; // @[model.scala 23:11:@71.4]
  wire  q_f3_history_reset; // @[model.scala 23:11:@71.4]
  wire  q_f3_history_io_enq_ready; // @[model.scala 23:11:@71.4]
  wire  q_f3_history_io_enq_valid; // @[model.scala 23:11:@71.4]
  wire [4:0] q_f3_history_io_enq_bits; // @[model.scala 23:11:@71.4]
  wire  q_f3_history_io_deq_ready; // @[model.scala 23:11:@71.4]
  wire  q_f3_history_io_deq_valid; // @[model.scala 23:11:@71.4]
  wire [4:0] q_f3_history_io_deq_bits; // @[model.scala 23:11:@71.4]
    
  ElasticReg q_f3_history ( // @[model.scala 23:11:@71.4]
    .clock(q_f3_history_clock),
    .reset(q_f3_history_reset),
      
    .io_enq_ready(q_f3_history_io_enq_ready),
    .io_enq_valid(q_f3_history_io_enq_valid),
    .io_enq_bits(q_f3_history_io_enq_bits),
      
    .io_deq_ready(q_f3_history_io_deq_ready),
    .io_deq_valid(q_f3_history_io_deq_valid),
    .io_deq_bits(q_f3_history_io_deq_bits)
  );
    
  assign io_i_ready = q_f3_history_io_enq_ready; // @[model.scala 27:13:@76.4]
  assign io_o_data = q_f3_history_io_deq_bits; // @[model.scala 30:12:@78.4]
  assign io_o_valid = q_f3_history_io_deq_valid; // @[model.scala 31:13:@79.4]
    
  assign q_f3_history_clock = clock; // @[:@72.4]
  assign q_f3_history_reset = io_i_reset; // @[:@73.4]
    //withReset(io.i_reset)的作用
    
    
    
  assign q_f3_history_io_enq_valid = io_i_valid; // @[model.scala 25:29:@74.4]
  assign q_f3_history_io_enq_bits = io_i_data; // @[model.scala 26:29:@75.4]
  assign q_f3_history_io_deq_ready = io_o_ready; // @[model.scala 29:29:@77.4]
endmodule
```

