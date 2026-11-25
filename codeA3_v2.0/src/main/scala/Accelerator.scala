import chisel3._
import chisel3.util._

class Accelerator extends Module {
  val io = IO(new Bundle {
    val start = Input(Bool())
    val done = Output(Bool())

    val address = Output(UInt (16.W))
    val dataRead = Input(UInt (32.W))
    val writeEnable = Output(Bool ())
    val dataWrite = Output(UInt (32.W))

  })

  //Write here your code
  val idle :: read :: done :: erode :: above :: below :: left :: right ::Nil = Enum (8)
  val stateReg = RegInit(idle)

  //Support registers
  val addressReg = RegInit(0.U(16.W))
  val dataReg = RegInit(0.U(32.W))

  //Default values
  io.writeEnable := false.B
  io.address := 0.U(16.W)
  io.dataWrite := dataReg
  io.done := false.B

  //FSMD switch
  switch(stateReg) {
    is(idle) {
      when(io.start) {
        stateReg := read
        addressReg := 0.U(16.W)
      }
    }
    is(read) {
      when(addressReg < 21.U || addressReg >= 379.U || addressReg === 60.U || addressReg === 40.U || addressReg === 80.U || addressReg === 100.U || addressReg === 120.U || addressReg === 140.U || addressReg === 160.U || addressReg === 180.U || addressReg === 200.U || addressReg === 220.U || addressReg === 240.U || addressReg === 260.U || addressReg === 280.U || addressReg === 300.U || addressReg === 320.U || addressReg === 340.U || addressReg === 360.U || addressReg === 49.U || addressReg === 69.U || addressReg === 89.U || addressReg === 109.U || addressReg === 129.U || addressReg === 149.U || addressReg === 169.U || addressReg === 189.U || addressReg === 209.U || addressReg === 229.U || addressReg === 249.U || addressReg === 269.U || addressReg === 289.U || addressReg === 309.U || addressReg === 329.U || addressReg === 349.U || addressReg === 369.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg
        dataReg := io.dataRead
        stateReg := erode
      }
    }
    is(erode) {
      when(dataReg === 0.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg - 20.U
        dataReg := io.dataRead
        stateReg := above
      }
    }
    is(above) {
      when(dataReg === 0.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg - 1.U
        dataReg := io.dataRead
        stateReg := left
      }
    }
    is(left) {
      when(dataReg === 0.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg + 1.U
        dataReg := io.dataRead
        stateReg := right
      }
    }
    is(right) {
      when(dataReg === 0.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg + 20.U
        dataReg := io.dataRead
        stateReg := below
      }
    }
    is(below) {
      when(dataReg === 0.U) {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 0.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }.otherwise {
        io.address := addressReg + 400.U
        io.writeEnable := true.B
        io.dataWrite := 255.U
        addressReg := addressReg + 1.U
        when(addressReg >= 400.U) {
          stateReg := done
        }.otherwise {
          stateReg := read
        }
      }
    }
    is(done) {
      io.done := true.B;
      stateReg := idle
    }
  }
}
