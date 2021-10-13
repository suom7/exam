package com.suom.exam.services;

import com.suom.exam.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class QuizService {
    private static final List<String> OPTIONS = List.of("A", "B", "C", "D");

    public List<Quiz> getAreaI() throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook(getInputStream("area-i.xlsx"));
        List<Quiz> sheet0 = fetchBySheet(wb.getSheetAt(0));
        List<Quiz> sheet1 = fetchBySheet(wb.getSheetAt(1));
        List<Quiz> sheet2 = fetchBySheet(wb.getSheetAt(2));
        List<Quiz> sheet3 = fetchBySheet(wb.getSheetAt(3));
        List<Quiz> sheet4 = fetchBySheet(wb.getSheetAt(4));
        // Closing the workbook
        wb.close();
        List<Quiz> joinedList = new ArrayList<>();
        Stream.of(sheet0, sheet1, sheet2, sheet3, sheet4).forEach(joinedList::addAll);
        return joinedList;
    }

    public List<Quiz> getAreaII() throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook(getInputStream("area-ii.xlsx"));
        List<Quiz> sheet0 = fetchBySheet(wb.getSheetAt(0));
        List<Quiz> sheet1 = fetchBySheet(wb.getSheetAt(1));
        List<Quiz> sheet2 = fetchBySheet(wb.getSheetAt(2));
        List<Quiz> sheet3 = fetchBySheet(wb.getSheetAt(3));
        List<Quiz> sheet4 = fetchBySheet(wb.getSheetAt(4));
        // Closing the workbook
        wb.close();
        List<Quiz> joinedList = new ArrayList<>();
        Stream.of(sheet0, sheet1, sheet2, sheet3, sheet4).forEach(joinedList::addAll);
        return joinedList;
    }

    public List<Quiz> getAreaIII() throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook(getInputStream("area-iii.xlsx"));
        List<Quiz> sheet0 = fetchBySheet(wb.getSheetAt(0));
        // Closing the workbook
        wb.close();
        return sheet0;
    }

    public List<Quiz> getAreaIV() throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook(getInputStream("area-iv.xlsx"));
        List<Quiz> sheet0 = fetchBySheet(wb.getSheetAt(0));
        List<Quiz> sheet1 = fetchBySheet(wb.getSheetAt(1));
        List<Quiz> sheet2 = fetchBySheet(wb.getSheetAt(2));
        List<Quiz> sheet3 = fetchBySheet(wb.getSheetAt(3));
        // Closing the workbook
        wb.close();
        List<Quiz> joinedList = new ArrayList<>();
        Stream.of(sheet0, sheet1, sheet2, sheet3).forEach(joinedList::addAll);
        return joinedList;
    }

    public List<Quiz> mergeArea123() throws IOException {
        List<Quiz> area1 = this.getAreaI();
        List<Quiz> area3 = this.getAreaIII();
        List<Quiz> area4 = this.getAreaIV();
        List<Quiz> merge = new ArrayList<>();
        Stream.of(area1, area3, area4).forEach(merge::addAll);
        return merge;
    }

    public FileInputStream getInputStream(String filename) throws IOException {
        Resource resource = new ClassPathResource(filename);
        return new FileInputStream(resource.getFile());
    }

    public List<Quiz> fetchBySheet(XSSFSheet sheet) {
        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Quiz> quizzes = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Now let's iterate over the columns of the current row
            Cell cell0 = row.getCell(0);
            String cell0Value = dataFormatter.formatCellValue(cell0);

            Cell cell1 = row.getCell(1);
            String cell1Value = StringUtils.remove(dataFormatter.formatCellValue(cell1), ' ');

            Cell cell2 = row.getCell(2);
            String cell2Value = dataFormatter.formatCellValue(cell2);

            Cell cell4 = row.getCell(4);
            String cell4Value = getNumericCellAsStr(cell4);

            Quiz existingQuiz = quizzes.stream()
                    .filter(f -> f.getNumber().equals(cell4Value))
                    .findFirst().orElse(null);

            if (Objects.nonNull(existingQuiz)) {
                if (OPTIONS.contains(cell1Value)) {
                    CellStyle cell1Style = cell1.getCellStyle();
                    Color color = cell1Style.getFillBackgroundColorColor();
                    if (Objects.nonNull(color)) {
                        existingQuiz.setAnswer(cell1Value);
                    }
                    Map<String, String> map = Objects.nonNull(existingQuiz.getOptions()) ?  existingQuiz.getOptions() : new HashMap<>();
                    map.put(cell1Value, cell2Value);
                    existingQuiz.setOptions(map);
                } else {
                    existingQuiz.setAdditional(cell2Value);
                }
            } else if (isNumeric(cell0Value)) {
                Quiz quiz = new Quiz();
                quiz.setNumber(cell0Value);
                quiz.setQuestion(cell1Value);
                quizzes.add(quiz);
            }
        }

        log.info("size {}", quizzes.size());
        return quizzes;
    }

    private String getNumericCellAsStr(Cell cell) {
        try {
            return StringUtils.remove(String.valueOf(cell.getNumericCellValue()), ".0");
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public static void main(String[] args) throws IOException {
        QuizService service = new QuizService();
        log.info("Area I");
        service.getAreaI();
        log.info("Area II");
        service.getAreaII();
        log.info("Area III");
        service.getAreaIII();
        log.info("Area IV");
        service.getAreaIV();
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

