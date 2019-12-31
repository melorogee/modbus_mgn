//package com.xwtec.modules.switchcontrol.tools;
//import org.apache.commons.lang3.StringUtils;
//
//import com.serotonin.modbus4j.ModbusFactory;
//import com.serotonin.modbus4j.ModbusMaster;
//import com.serotonin.modbus4j.code.DataType;
//import com.serotonin.modbus4j.exception.ErrorResponseException;
//import com.serotonin.modbus4j.exception.ModbusInitException;
//import com.serotonin.modbus4j.exception.ModbusTransportException;
//import com.serotonin.modbus4j.ip.IpParameters;
//import com.serotonin.modbus4j.locator.BaseLocator;
//import com.serotonin.modbus4j.msg.ModbusResponse;
//import com.serotonin.modbus4j.msg.WriteRegisterRequest;
//import com.serotonin.modbus4j.msg.WriteRegistersRequest;
//
//public class ModBus4JTCPClient {
//
//
//        private ModbusFactory modbusFactory;
//
//        public ModbusFactory getModbusFactory() {
//            if (modbusFactory == null) {
//                modbusFactory = new ModbusFactory();
//            }
//            return modbusFactory;
//        }
//
//        /**
//         *
//         * @param ip
//         * @param port
//         * @return
//         * @throws ModbusInitException
//         */
//        public ModbusMaster getMaster(String ip, int port) throws ModbusInitException {
//            ModbusMaster m = null;
//            IpParameters tcpParameters = new IpParameters();
//            tcpParameters.setHost(ip);
//            tcpParameters.setPort(port);
//            m = getModbusFactory().createTcpMaster(tcpParameters, false);
//            m.setTimeout(2000);
//            m.init();
//            return m;
//        }
//
//        /**
//         * Holding Register类型
//         *
//         * @param master
//         * @param modbusId
//         * @param registerId
//         * @param registerNumber
//         * @param analysisFlag
//         * @return
//         * @throws ModbusTransportException
//         * @throws ErrorResponseException
//         * @throws ModbusInitException
//         */
//        public Number readHoldingRegister(ModbusMaster master, int modbusId, int registerId, int registerNumber,
//                                          String analysisFlag) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//            Number value = null;
//            if ("u".equals(analysisFlag)) {
//                value = holdingRegister_UNSIGNED(master, modbusId, registerId, registerNumber);
//            } else if ("s".equals(analysisFlag)) {
//                value = holdingRegister_SIGNED(master, modbusId, registerId, registerNumber);
//            }
//            return value;
//        }
//
//        public Number holdingRegister_UNSIGNED(ModbusMaster master, int modbusId, int registerId, int registerNumber)
//                throws ModbusTransportException, ErrorResponseException {
//            BaseLocator<Number> loc = BaseLocator.holdingRegister(modbusId, registerId - 1,
//                    (registerNumber == 1) ? DataType.TWO_BYTE_INT_UNSIGNED : DataType.FOUR_BYTE_INT_UNSIGNED);
//
//            return master.getValue(loc);
//        }
//
//        public Number holdingRegister_SIGNED(ModbusMaster master, int modbusId, int registerId, int registerNumber)
//                throws ModbusTransportException, ErrorResponseException {
//            return master.getValue(BaseLocator.holdingRegister(modbusId, registerId - 1,
//                    (registerNumber == 1) ? DataType.TWO_BYTE_INT_SIGNED : DataType.FOUR_BYTE_INT_SIGNED));
//        }
//
//        // Input Registers
//        public void readInputRegisters(ModbusMaster master, int registerId, int registerNumber, int modbusId,
//                                       String analysisFlag) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//            Number value = null;
//            //z代表是正数，s代表可能是负数
//            if ("z".equals(analysisFlag)) {
//                value = inputRegister_UNSIGNED(master, modbusId, registerId, registerNumber);
//            } else if ("f".equals(analysisFlag)) {
//                value = inputRegister_SIGNED(master, modbusId, registerId, registerNumber);
//            }
//        }
//
//        public Number inputRegister_UNSIGNED(ModbusMaster master, int modbusId, int registerId, int registerNumber)
//                throws ModbusTransportException, ErrorResponseException {
//            return master.getValue(BaseLocator.inputRegister(modbusId, registerId - 1,
//                    (registerNumber == 1) ? DataType.TWO_BYTE_INT_UNSIGNED : DataType.FOUR_BYTE_INT_UNSIGNED));
//        }
//
//        public Number inputRegister_SIGNED(ModbusMaster master, int modbusId, int registerId, int registerNumber)
//                throws ModbusTransportException, ErrorResponseException {
//            return master.getValue(BaseLocator.inputRegister(modbusId, registerId - 1,
//                    (registerNumber == 1) ? DataType.TWO_BYTE_INT_SIGNED : DataType.FOUR_BYTE_INT_SIGNED));
//        }
//
//        /**
//         * 写入寄存器
//         *
//         * @param master
//         * @param registerId
//         * @param modbusId
//         * @param sdata
//         * @return
//         * @throws ModbusTransportException
//         * @throws ModbusInitException
//         */
//        public boolean writeRegister(ModbusMaster master, int registerId, int modbusId, short sdata)
//                throws ModbusTransportException, ModbusInitException {
//            // 创建请求对象
//            WriteRegisterRequest request = new WriteRegisterRequest(modbusId, registerId - 1, sdata);
//            // 发送请求并获取响应对象
//            ModbusResponse response = master.send(request);
//            if (response.isException()) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//
//        public boolean writeRegisters(ModbusMaster master, int registerId, int modbusId, short[] sdata)
//                throws ModbusTransportException, ModbusInitException {
//            // 创建请求对象
//            WriteRegistersRequest request = new WriteRegistersRequest(modbusId, registerId - 1, sdata);
//            // 发送请求并获取响应对象
//            ModbusResponse response = master.send(request);
//            if (response.isException()) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//
//        public void writeHoldingRegister(ModbusMaster master, int registerId, int modbusId, Number writeValue, int dataType)
//                throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//            BaseLocator<Number> locator = BaseLocator.holdingRegister(modbusId, registerId - 1,
//                    (dataType == 1) ? DataType.TWO_BYTE_INT_UNSIGNED : DataType.FOUR_BYTE_INT_UNSIGNED);
//            master.setValue(locator, writeValue);
//        }
//
//
//    public static void main(String[] args) throws Exception {
//        int registerId=100;
//        int modbusId=1;
//        ModBus4JTCPClient modbusTcp = new ModBus4JTCPClient();
//
//        ModbusMaster master = modbusTcp.getMaster("localhost", 502);	//192.168.1.108
//
//        modbusTcp.readInputRegisters(master, registerId, 1, modbusId, "u");
//    }
//
//}
//
//
