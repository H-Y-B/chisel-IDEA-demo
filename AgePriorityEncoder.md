# AgePriorityEncoder

```scala
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val temp_vec = Vec(false.B, //下标从0开始   |
                     false.B, //            |
                     false.B, //            |
                     false.B, //            |
                     false.B, //            |
                     true.B,  //            v        下标5（从head开始的第一个true）
                     true.B,
                     false.B,
                     false.B,
                     false.B,
                     false.B, 
                     false.B, //
                     true.B,  //
                     false.B, // <-----head   (head也包含在查找中)
                     false.B) //           |
                              //           v

  val  head    = UInt("b1101")//head为13
  io.out :=AgePriorityEncoder(temp_vec,head)
```

```verilog
  assign io_out = 5'h5; // @[model.scala 13:10:@85.4]
```



### 没有true

```scala
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val temp_vec = Vec(false.B, false.B,false.B,false.B,false.B,false.B, false.B)
  //没有true的话，直接指向最后一个，和head没有关系
  val  head    = UInt("b11")
  io.out :=AgePriorityEncoder(temp_vec,head)
```

```verilog
  assign io_out = 5'h6; // @[model.scala 13:10:@45.4]
```

