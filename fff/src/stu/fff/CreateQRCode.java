package stu.fff;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;


public class CreateQRCode {
	public static void main(String[] args) throws Exception {
		int version=20;
		int imageSize=485;
		//int height=127;
		Qrcode qrcode=new Qrcode();
		//String content="http://www.dijiaxueshe.com";

		qrcode.setQrcodeErrorCorrect('H');
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(version);
		
		BufferedImage bufferedImage=new BufferedImage(imageSize,imageSize,BufferedImage.TYPE_INT_RGB);
		Graphics2D gs=bufferedImage.createGraphics();
		gs.setBackground(Color.white);
		gs.setColor(Color.pink);
		//清除画布
		gs.clearRect(0,0,imageSize,imageSize);
		int pixoff=2;//偏移量
		//设置二维码内容
		String content="BEGIN:VCARD\n"+
		"N:付\n"+
		"FN:大美\n"+
		"ORG:中国少先队\n"+
		"TITLE:幼儿园小朋友\n" + 
		"ADR;WORK:秦皇岛\n" + 
		"ADR;HOME:古都\n" + 
		"TEL;CELL,VOICE:15076055156\n" +  
		"URL;WORK;:http://www.icbc.com.cn\n" + 
		"EMAIL;INTERNET,HOME:1227163928@qq.com\n" + 
		"END:VCARD";
		byte[] data=content.getBytes("utf-8");
		boolean[][] qrdata=qrcode.calQrcode(data);
	
		int startR=249,startG=173,startB=213;
		int endR=164,endG=250,endB=230;
		
		//偏移量
		//int pixoff = 1;
		boolean [][] s = qrcode.calQrcode(data);
		for(int i=0;i<qrdata.length;i++){
			for(int j=0;j<qrdata.length;j++){
				if(qrdata[i][j]){
					//Random rand=new Random();
					int num1=startR+(endR-startR)*(i+1)/qrdata.length;
					int num2=startG+(endG-startG)*(i+1)/qrdata.length;
					int num3=startB+(endB-startB)*(i+1)/qrdata.length;
					Color color=new Color(num1,num2,num3);
					gs.setColor(color);
					gs.fillRect(i*5+pixoff, j*5+pixoff, 5, 5);
				}
			}
		}
		//BufferedImage logo=ImageIO.read(new File("D:/logo.jpg"));
		BufferedImage logo=scale("D:/qq.jpg",70,70,true);
		int o=(imageSize-logo.getHeight())/2;
		gs.drawImage(logo,o,o,70,70,null);
		//int logoSize=imageSize/4;
		//设置logo图片在二维码中心
		//int o=(imageSize-logoSize)/2;
		//gs.drawImage(logo, o, o, logoSize, logoSize, null);
		gs.dispose();//关闭绘图
		bufferedImage.flush();//输出缓冲中的资源缓冲流的flush方法的作用是强制将缓冲内部缓冲区已经缓存的数据一次性写出。
		try{
			ImageIO.write(bufferedImage,"png",new File("D:/qrcode.png"));
		}catch (IOException e){
			e.printStackTrace();
			System.out.println("产生了问题");
		}
		System.out.println("二维码生成了！");
	}

private static BufferedImage scale(String logoPath,int width,int height,boolean hasFiller)throws Exception{
	double ratio = 0.0;
	File file = new File(logoPath);
	BufferedImage srcImage = ImageIO.read(file);
	Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
	
	//计算比例
	if((srcImage.getHeight()>height) || (srcImage.getWidth()>width)){
		if(srcImage.getHeight()>srcImage.getWidth()){
			ratio = (new Integer(height)).doubleValue()/srcImage.getHeight();
		}else{
			ratio = (new Integer(width)).doubleValue()/srcImage.getWidth();
		}
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio),null);
		destImage = op.filter(srcImage,null);
	}
	if(hasFiller){//补白
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setColor(Color.white);
		graphic.fillRect(0, 0, width, height);
		if(width==destImage.getWidth(null)){
			graphic.drawImage(destImage,0,(height-destImage.getHeight(null))/2,destImage.getWidth(null),destImage.getHeight(null),Color.white,null);
		}else{
			graphic.drawImage(destImage,(width-destImage.getWidth(null))/2,0,destImage.getWidth(null),destImage.getHeight(null),Color.white,null);
		}
		graphic.dispose();
		destImage=image;
	}
	return (BufferedImage) destImage;
	}
}


