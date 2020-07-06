module model( // @[:@3.2]
  input        clock, // @[:@4.4]
  input        reset, // @[:@5.4]
  output [4:0] io_out // @[:@6.4]
);
  assign io_out = 5'h6; // @[model.scala 13:10:@45.4]
endmodule
