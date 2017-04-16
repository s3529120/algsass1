import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class tests {
	public final static String[] type= {"education","hospital","restaurant"};
	public final static String[] AD= {"A","D"};

	public static void main(String[] args) {
		BufferedWriter bw=null;
		FileWriter fw=null;
		Random rand= new Random();
		String[] arg=new String[4];
		Long[] nres,kdres;
		Long startTime,endTime,navg,kdavg;
		File file;
		
		//base data
				file = new File("testing/datafile.txt");
				try {
					fw=new FileWriter(file);
				} catch (IOException e) {
				}
				bw=new BufferedWriter(fw);
				String write="";
				for(int i=0;i<250;i++){
					write=write.concat(String.format("id%03d %s %.2f %.2f\n", 
							i,
							type[rand.nextInt((2-0)+1)+0],
							-75.00+(-25.00+75.00)*rand.nextDouble(),
							125.00+(175.00-125.00)*rand.nextDouble()));
				}
				try {
					bw.write(write);
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		for(int j=1;j<6;j++){
			System.out.println("Run: "+j);
		
		
		
		//Scenario 1 searches
		
		//Generate command file
		file = new File("testing/inputcommand.txt");
		try {
			fw=new FileWriter(file);
		} catch (IOException e) {
		}
		bw=new BufferedWriter(fw);
		write="";
		for(int i=0;i<250;i++){
			write=write.concat(String.format("S %s %.2f %.2f %d\n", 
					type[rand.nextInt((2-0)+1)+0],
					-75.00+(-25.00+75.00)*rand.nextDouble(),
					125.00+(175.00-125.00)*rand.nextDouble(),j*10));
		}
		try {
			bw.write(write);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//run naive
		nres = new Long[10];
		arg[0]="naive";
		arg[1]="testing/datafile.txt";
		arg[2]="testing/inputcommand.txt";
		arg[3]="testing/naiveresult.txt";
		System.out.println("NaiveNN");
		for(int i=0;i<10;i++){
			startTime = System.currentTimeMillis();
			NearestNeighFileBased.main(arg);
			endTime=System.currentTimeMillis();
			nres[i]=endTime-startTime;
		}
		//run kd
				kdres = new Long[10];
				arg[0]="kdtree";
				arg[1]="testing/datafile.txt";
				arg[2]="testing/inputcommand.txt";
				arg[3]="testing/kdresult.txt";
				System.out.println("KDNN");
				for(int i=0;i<10;i++){
					startTime = System.currentTimeMillis();
					NearestNeighFileBased.main(arg);
					endTime=System.currentTimeMillis();
					kdres[i]=endTime-startTime;
				}
		file = new File("testing/times.txt");
		try {
			fw=new FileWriter(file,true);
		} catch (IOException e) {
		}
		bw=new BufferedWriter(fw);
		write="";
		navg=0L;
		kdavg=0L;
		for(int a=0;a<10;a++){
			navg=navg+nres[a];
			kdavg=kdavg+kdres[a];
		}
		write=write.concat("NaiveNN: ");
		write=write+(j*10);
		write=write.concat("\n");
		for(int x=0;x<10;x++){
			write=write+nres[x]+" ";
		}
		write=write+" -AVG: "+(navg/10)+"\n";
		write=write.concat("KDTreeNN: ");
		write=write+(j*10);
		write=write.concat("\n");
		for(int x=0;x<10;x++){
			write=write+kdres[x]+" ";
		}
		write=write+" -AVG: "+(kdavg/10)+"\n";
		try {
			bw.write(write);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		//scenario 2 add/remove
		for(int j=1;j<11;j++){
			System.out.println("Run: "+j);
			file = new File("testing/inputcommand.txt");
			try {
				fw=new FileWriter(file);
			} catch (IOException e) {
			}
			bw=new BufferedWriter(fw);
			write="";
			int r;
			Double x,y;
			for(int i=0;i<j*1000;i++){
				r=rand.nextInt((1-0)+1)+0;
				x=-75.00+(-25.00+75.00)*rand.nextDouble();
				y=125.00+(175.00-125.00)*rand.nextDouble();
				write=write.concat(String.format("A id%03d %s %.2f %.2f\n",
						500+i,type[r],x,y));
				write=write.concat(String.format("D id%03d %s %.2f %.2f\n",
						500+i,type[r],x,y));
			}
			try {
				bw.write(write);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//run naive
			nres = new Long[100];
			arg[0]="naive";
			arg[1]="testing/datafile.txt";
			arg[2]="testing/inputcommand.txt";
			arg[3]="testing/naiveresult.txt";
			System.out.println("NaiveAD");
			for(int i=0;i<100;i++){
				startTime = System.currentTimeMillis();
				NearestNeighFileBased.main(arg);
				endTime=System.currentTimeMillis();
				nres[i]=endTime-startTime;
			}
			//run kd
					kdres = new Long[100];
					arg[0]="kdtree";
					arg[1]="testing/datafile.txt";
					arg[2]="testing/inputcommand.txt";
					arg[3]="testing/kdresult.txt";
					System.out.println("KDAD");
					for(int i=0;i<100;i++){
						startTime = System.currentTimeMillis();
						NearestNeighFileBased.main(arg);
						endTime=System.currentTimeMillis();
						kdres[i]=endTime-startTime;
					}
					file = new File("testing/times.txt");
					try {
						fw=new FileWriter(file,true);
					} catch (IOException e) {
					}
					bw=new BufferedWriter(fw);
					write="";
					navg=0L;
					kdavg=0L;
					for(int a=0;a<100;a++){
						navg=navg+nres[a];
						kdavg=kdavg+kdres[a];
					}
					write=write.concat("NaiveAD: ");
					write=write+(j*10);
					write=write.concat("\n");
					for(int a=0;a<100;a++){
						write=write+nres[a]+" ";
					}
					write=write+" -AVG: "+(navg/100)+"\n";
					write=write.concat("KDTreeAD: ");
					write=write+(j*10);
					write=write.concat("\n");
					for(int a=0;a<100;a++){
						write=write+kdres[a]+" ";
					}
					write=write+" -AVG: "+(kdavg/100)+"\n";
					try {
						bw.write(write);
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		//Scenario 3 - datafile size
		file = new File("testing/inputcommand.txt");
		try {
			fw=new FileWriter(file);
		} catch (IOException e) {
		}
		bw=new BufferedWriter(fw);
		write="";
		for(int i=0;i<100;i++){
			write=write.concat(String.format("S %s %.2f %.2f %d\n", 
					type[rand.nextInt((2-0)+1)+0],
					-75.00+(-25.00+75.00)*rand.nextDouble(),
					125.00+(175.00-125.00)*rand.nextDouble(),10));
		}
		try {
			bw.write(write);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int j=1;j<11;j++){
			System.out.println("Run: "+j);
			file = new File("testing/datafile.txt");
			try {
				fw=new FileWriter(file);
			} catch (IOException e) {
			}
			bw=new BufferedWriter(fw);
			write="";
			for(int i=0;i<j*100;i++){
				write=write.concat(String.format("id%03d %s %.2f %.2f\n", 
						i,
						type[rand.nextInt((2-0)+1)+0],
						-75.00+(-25.00+75.00)*rand.nextDouble(),
						125.00+(175.00-125.00)*rand.nextDouble()));
			}
			try {
				bw.write(write);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//run naive
			nres = new Long[10];
			arg[0]="naive";
			arg[1]="testing/datafile.txt";
			arg[2]="testing/inputcommand.txt";
			arg[3]="testing/naiveresult.txt";
			System.out.println("NaiveDF");
			for(int i=0;i<10;i++){
				startTime = System.currentTimeMillis();
				NearestNeighFileBased.main(arg);
				endTime=System.currentTimeMillis();
				nres[i]=endTime-startTime;
			}
			//run kd
					kdres = new Long[10];
					arg[0]="kdtree";
					arg[1]="testing/datafile.txt";
					arg[2]="testing/inputcommand.txt";
					arg[3]="testing/kdresult.txt";
					System.out.println("KDDF");
					for(int i=0;i<10;i++){
						startTime = System.currentTimeMillis();
						NearestNeighFileBased.main(arg);
						endTime=System.currentTimeMillis();
						kdres[i]=endTime-startTime;
					}
			file = new File("testing/times.txt");
			try {
				fw=new FileWriter(file,true);
			} catch (IOException e) {
			}
			bw=new BufferedWriter(fw);
			write="";
			navg=0L;
			kdavg=0L;
			for(int a=0;a<10;a++){
				navg=navg+nres[a];
				kdavg=kdavg+kdres[a];
			}
			write=write.concat("NaiveDF: ");
			write=write+(j*10);
			write=write.concat("\n");
			for(int x=0;x<10;x++){
				write=write+nres[x]+" ";
			}
			write=write+" -AVG: "+(navg/10)+"\n";
			write=write.concat("KDTreeDF: ");
			write=write+(j*10);
			write=write.concat("\n");
			for(int x=0;x<10;x++){
				write=write+kdres[x]+" ";
			}
			write=write+" -AVG: "+(kdavg/10)+"\n";
			try {
				bw.write(write);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
