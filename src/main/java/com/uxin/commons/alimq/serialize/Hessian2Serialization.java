package com.uxin.commons.alimq.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ellis.luo
 * @date 16/11/4 下午7:52
 * @description
 */
public class Hessian2Serialization
{
    private static final Logger logger = LoggerFactory.getLogger(Hessian2Serialization.class);

    public static <T> byte[] serialize(T data)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            Hessian2Output out = new Hessian2Output(bos);
            out.writeObject(data);
            out.flush();
        }
        catch (IOException e)
        {
            logger.error("Hessian2Serialization serialize " + data.toString() + " error");
            return null;
        }

        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<T> clz)
    {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));

        try
        {
            return (T) input.readObject(clz);
        }
        catch (IOException e)
        {
            logger.error("Hessian2Serialization deserialize " + clz.getSimpleName() + " error");
            return null;
        }

    }

}
