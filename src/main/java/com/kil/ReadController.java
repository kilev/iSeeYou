package com.kil;


import java.io.*;

public class ReadController {
    public ReadController(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("BRNCH")) {
                    break;
                }
                addNode(line);
            }
            while ((line = reader.readLine()) != null) {
                if (line.equals("LOSS")) {
                    break;
                }
                addBranch(line);
            }
            line = reader.readLine();
            Logic.LOSS = Double.parseDouble(line);

            Logic.coordConvert = new CoordConvert(Logic.nodeList);
            Logic.coordConvert.changeCoords();
            reader.close();
        }
    }

    private void addNode(String line) {
        String[] info = line.split(" ");
        CityNode node = new CityNode(info[0], Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]),
                Double.parseDouble(info[4]), Double.parseDouble(info[5]), Integer.parseInt(info[6]));
        Logic.nodeList.add(node);
    }

    private  void addBranch(String line) {
        String[] info = line.split(" ");
        Branch branch = new Branch(Integer.parseInt(info[0]), Integer.parseInt(info[1]), Double.parseDouble(info[2]),
                Double.parseDouble(info[3]), Double.parseDouble(info[4]), Double.parseDouble(info[5]), Integer.parseInt(info[6]));
        Logic.branchList.add(branch);
    }
}

