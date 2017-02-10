import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RealSpider {
	private final String HomePage = "https://share.dmhy.org";
	private final String NextPage = HomePage + "/topics/list/page/";
	private String Dir;
	
	public RealSpider(String Dir) {
		this(Dir,1,3800);
	}
	
	public RealSpider(String Dir,int StartPage,int EndPage) {
		this.Dir = Dir;
		this.StartPage = StartPage;
		this.EndPage = EndPage;
	}
	
	public String ReadPage(String url) {
		URL ThisPage;
		while(true) {
			try {
				ThisPage = new URL(url);
				URLConnection connection = ThisPage.openConnection();
				connection.setConnectTimeout(5 * 1000);
				connection.setReadTimeout(5 * 1000);
				BufferedReader PageReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				StringBuilder html = new StringBuilder();
				String temp;
				while ((temp = PageReader.readLine()) != null) {
					html.append(temp);
				}
				PageReader.close();
				return html.toString();
			} catch (SocketException e) {
				System.err.println(e.getMessage());
				continue;
			} catch (IOException e) { 
				System.err.println(e.getMessage());
				continue;
			}
		}
			
	}
	
	public Resource getResourceByHtml(String html) {
		Pattern p = Pattern.compile("\\s+?\\S{0,3}<font color=.?\\w+?>(\\W{1,5})</font>.+?"
				+ "target=\"_blank\" >\\s+(.+?)</a>.+?(magnet:\\?xt=urn:btih:.+?)\">&nbsp;");
		Matcher m = p.matcher(html);
		Resource re = new Resource();
		while (m.find()) {
			re.Category.add(m.group(1));
			re.DirName.add(m.group(2).replaceAll("[\\\\?\"/<>*|:／]+", " ").trim());
			re.torrent.add(m.group(3));
		}
		return re;
	}
	
	public void Download(Resource re) {
		if(!re.IsEqual()) {
			System.err.println("数量不符");
		} else {
			for (int i = 0; i < re.torrent.size(); i++) {
				File file = new File(Dir + "\\" + re.Category.get(i) + 
						"\\" + re.DirName.get(i) + "\\" + "torrent.txt");
				file.getParentFile().mkdirs();
				if (!file.exists()) {
					try {
						file.createNewFile();
						FileWriter out = new FileWriter(file);
						out.write(re.torrent.get(i));
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(file);
					}
				} 
			}
		}
	}
	
	public String getHomePage() {
		return HomePage;
	}
	
	public String getNextPage(int Page) {
		return NextPage + Integer.toString(Page);
	}
	
	
	public static void main(String[] args) {
		RealSpider spider = new RealSpider("F:\\dmhy");
		String html;
		for (int i = 1; i <= 1000; i++) { 
			System.out.println("Page:" + i);
			if (i == 1) {
				html = spider.ReadPage(spider.getHomePage());
			} else {
				html = spider.ReadPage(spider.getNextPage(i));
			}
			Resource re = spider.getResourceByHtml(html);
			spider.Download(re);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


class Resource {
	public List<String> Category;
	public List<String> DirName;
	public List<String> torrent;
	
	public Resource() {
		DirName = new ArrayList<String>();
		torrent = new ArrayList<String>();
		Category = new ArrayList<String>();
	}
	
	public boolean IsEqual() {
		if (Category.size() == torrent.size() 
				&& torrent.size() == DirName.size()) {
			return true;
		} else return false;
	}
}
