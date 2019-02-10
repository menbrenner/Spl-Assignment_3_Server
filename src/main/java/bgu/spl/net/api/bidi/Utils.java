package bgu.spl.net.api.bidi;

public class Utils {
    public static short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public static byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public static byte[] Concatenate(byte[] array1, byte[] array2 )
    {
        int len1=array1.length;
        int len2=array2.length;
        byte[] resultArray = new byte[len1 + len2];
        System.arraycopy(array1, 0, resultArray, 0, len1);
        System.arraycopy(array2, 0, resultArray, len1, len2);
        return resultArray;
    }


}
