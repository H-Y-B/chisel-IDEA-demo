# PriorityEncoder

```scala
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val  temp_vec = UInt("b00010100000000")//从右往左，下标从0开始
  io.out :=PriorityEncoder(temp_vec)
```

```verilog
  assign io_out = 5'h8; // @[model.scala 15:10:@18.4]
```



### Vec类型

```scala
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val temp_vec = Vec(false.B, //从下标0开始
                     false.B,
                     false.B,
                     false.B,
                     false.B,
                     true.B, //第一个true，下标5
                     true.B,
                     false.B,false.B,false.B,false.B, false.B,true.B,false.B, false.B)

io.out :=PriorityEncoder(temp_vec)
```

```verilog
  assign io_out = 5'h5; // @[model.scala 13:10:@38.4]
```





### 没有true

```scala
  val io=IO(new Bundle{
    val out=Output(UInt(5.W))
  })
  val temp_vec = Vec(false.B, false.B,false.B,false.B,false.B,false.B, false.B)
  //没有true的话，直接指向最后一个
  io.out :=PriorityEncoder(temp_vec)
```

```
  assign io_out = 5'h6; // @[model.scala 13:10:@22.4]
```

