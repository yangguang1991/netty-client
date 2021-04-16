package com.client.server;

import com.alibaba.fastjson.JSONObject;
import com.ymdx.cpmp.common.constant.Constant;
import com.ymdx.cpmp.common.util.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: kevin yang
 * @Description: 这个只使用63200301 来进行切割，不然会出现问题
 * @Date: create in 2020/9/25 14:25
 */
@Slf4j
public class CpmpMsgDecoder1 extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        in.markReaderIndex();//标记可读的位置
        log.info("CpmpMsgDecoder in.readableBytes()=" + in.readableBytes());
        log.info("ctx =" + ctx);
        int length = in.readableBytes();
        if (length < 16) {  //至少得有长度字段,因为报文头的，不然直接return
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];//有多少就读多少
        in.readBytes(data);//读取数据
        String recvStr = HexStringUtils.toHexString(data);

        log.info("recvStr="+recvStr);
        in.setIndex(length, length);// 长度不够的时候，将读索引复位，同时将写索引放在能读取的最大位置
    }
}
