
public class Multithread implements Runnable{
	private int Start;
	private int End;
	
	public Multithread() {
	}
	
	public Multithread(int Start, int End) {
		this.Start = Start;
		this.End = End;
	}
	
	public void run() {
		RealSpider spider = new RealSpider("F:\\dmhy");
		if (Start != 0 && End != 0) {
			spider.StartSpider(Start, End);
		} else {
			spider.StartSpider();
		}
	}
}
