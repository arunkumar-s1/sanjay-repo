package com.msf.libsb.appconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class SymbolTranslation 
{

	private static Map<String,Symbol> symbolMap=new HashMap<String,Symbol>() ;
	
	public static void loadMap(String folderPath) throws Exception 
	{
		File folder=new File(folderPath);
		File[] files=folder.listFiles();
		for(File file: files) 
		{
			try
			{
				String exch="";
				String filename=file.getName();
				if(filename.contains("NSE")) 
				{
					exch="NSE";
				}
				else if(filename.contains("BSE"))
				{
					exch="BSE";
				}
				BufferedReader br = new BufferedReader(new FileReader(file));
	
		        String line = br.readLine(); // Reading header, Ignoring
	
		        while ((line = br.readLine()) != null) {
		            String[] fields = line.split(",");
		            String scrip = fields[0];
		            String symNameEng = fields[1];
		            String symNameHin = fields[2];
		            String comNameEng = fields[3];
		            String comNameHin = fields[4];
		            Symbol symbol=new Symbol(symNameHin,symNameEng,comNameHin,comNameEng);
		            symbolMap.put(scrip+"_"+exch, symbol);
		        }
		        br.close();
			}
			catch(Exception e) 
			{
				
			}
				
		}
	       
	}

	public static Map<String, Symbol> getSymbolMap() 
	{
		return symbolMap;
	}
	
}
