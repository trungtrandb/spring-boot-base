package site.code4fun.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import site.code4fun.entity.*;
import site.code4fun.entity.dto.ClassDTO;
import site.code4fun.entity.dto.PointDTO;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.repository.jdbc.JLessionRepository;
import site.code4fun.util.StringUtils;

@Service
public class ClassService extends BaseService {

    public List<Classes> getByGroupId(Long id) {
        return null != id ? classRepository.findByGroupId(Collections.singletonList(id)) : getCurrentClasses();
    }

    public Boolean authorizeClass(Long groupClassId) {
        return true;
    }

    public Classes insert(ClassDTO c) throws Exception {
        if (StringUtils.isNull(c.getName())) throw new Exception("Tên lớp là bắt buộc nhập");
        if (null == c.getGroupClassId()) throw new Exception("Khối lớp là bắt buộc chọn");
        if (null == c.getOwnerId()) throw new Exception("Giáo viên chủ nhiệm là bắt buộc chọn");
        Optional<GroupClass> group = groupClassRepository.findById(c.getGroupClassId());
        Optional<User> user = userRepository.findById(c.getOwnerId());

        if (group.isPresent() && user.isPresent()){
            Classes clazz = Classes.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .note(c.getNote())
                    .groupClass(group.get())
                    .schoolYear(c.getSchoolYear())
                    .status(c.getStatus())
                    .owner(user.get())
                    .build();
            return classRepository.saveAndFlush(clazz);
        }
        throw new Exception("Khối lớp hoặc giáo viên không tồn tại");
    }

    public void delete(Long id) throws Exception {
        Optional<Classes> item = classRepository.findById(id);
        if (!item.isPresent()) throw new Exception("Class not found!");
        classRepository.deleteById(id);
    }

    public List<PointDTO> getPoint(Long classId, Long subjectId, Byte sem) {
        List<StudentDTO> lstStudent = jStudentRepository.findStudentByClassId(Collections.singletonList(classId), null);
        List<Long> studentIds = lstStudent.stream().map(StudentDTO::getId).collect(Collectors.toList());
        Map<Long, PointDTO> mapPoint = jPointRepository.getPoint(StringUtils.stringFromList(studentIds), subjectId, sem);

        return lstStudent.stream().map(_st -> {
            PointDTO dto;
            if (mapPoint.containsKey(_st.getId())) {
                dto = mapPoint.get(_st.getId());
            } else {
                dto = new PointDTO();
                dto.setSubjectId(subjectId);
                dto.setSem(sem);
            }
            dto.setStudentId(_st.getId());
            dto.setStudentName(_st.getName());
            dto.setStudentCode(_st.getStudentCode());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<Point> updatePoint(PointDTO point) {
        String multi1 = StringUtils.cleanToFloat(point.getPointMulti1());
        String multi2 = StringUtils.cleanToFloat(point.getPointMulti2());
        String multi3 = StringUtils.cleanToFloat(point.getPointMulti3());

        String[] lstPointMulti1 = multi1.split(" ");
        String[] lstPointMulti2 = multi2.split(" ");
        String[] lstPointMulti3 = multi3.split(" ");
        Long currentId = getCurrentId();

        pointRepository.deleteOldPoint(point.getStudentId(), point.getSubjectId(), point.getSem());
        List<Point> lstPoint = new ArrayList<>();
        for (String _item : lstPointMulti1) {
            if (StringUtils.isNull(_item)) continue;
            Point newPoint = new Point();
            newPoint.setStudentId(point.getStudentId());
            newPoint.setSubjectId(point.getSubjectId());
            newPoint.setMultiple((byte) 1);
            newPoint.setPoint(StringUtils.round1(_item));
            newPoint.setCreatedBy(currentId);
            newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            newPoint.setSem(point.getSem());
            lstPoint.add(newPoint);
        }

        for (String _item : lstPointMulti2) {
            if (StringUtils.isNull(_item)) continue;
            Point newPoint = new Point();
            newPoint.setStudentId(point.getStudentId());
            newPoint.setSubjectId(point.getSubjectId());
            newPoint.setMultiple((byte) 2);
            newPoint.setPoint(StringUtils.round1(_item));
            newPoint.setCreatedBy(currentId);
            newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            newPoint.setSem(point.getSem());
            lstPoint.add(newPoint);
        }

        for (String _item : lstPointMulti3) {
            if (StringUtils.isNull(_item)) continue;
            Point newPoint = new Point();
            newPoint.setStudentId(point.getStudentId());
            newPoint.setSubjectId(point.getSubjectId());
            newPoint.setMultiple((byte) 3);
            newPoint.setPoint(StringUtils.round1(_item));
            newPoint.setCreatedBy(currentId);
            newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            newPoint.setSem(point.getSem());
            lstPoint.add(newPoint);
        }

        pointRepository.saveAll(lstPoint);
        return lstPoint;
    }

    public Optional<Classes> getById(Long id) {
        return classRepository.findById(id);
    }

    public String exportPointClass(Long classId, Long subjectId, Byte sem, Byte numOfTest) throws IOException {
        String path = "src/main/webapp/resources/excel/";
        File file = new File(path);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        int rowNum = 0;
        Organization org = getCurrentOrganization();
        Optional<Classes> clazz = getById(classId);

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 6));
        Row row = sheet.createRow(rowNum);
        Cell cell;
        cell = row.createCell(rowNum);
        cell.setCellValue("Trường: ");
        cell = row.createCell(2);
        cell.setCellValue(org.getName());

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 6));
        row = sheet.createRow(rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Địa chỉ:");
        cell = row.createCell(2);
        cell.setCellValue(org.getAddress());

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 6));
        row = sheet.createRow(rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Lớp:");
        cell = row.createCell(2);
        cell.setCellValue(clazz.get().getName());

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 6));
        row = sheet.createRow(rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Giáo viên chủ nhiệm:");
        cell = row.createCell(2);
        cell.setCellValue(clazz.get().getOwner().getFullName());

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 6));
        row = sheet.createRow(rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Kỳ học:");
        cell = row.createCell(sem);
        cell.setCellValue(numOfTest);

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 6));
        row = sheet.createRow(rowNum);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Điểm");

        row = sheet.createRow(++rowNum); // Hàng tiêu đề cột
        CellStyle styleHeader = workbook.createCellStyle();
        font = workbook.createFont();
        font.setBold(true);
        styleHeader.setFont(font);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        cell = row.createCell(0);
        cell.setCellValue("#");
        cell.setCellStyle(styleHeader);

        cell = row.createCell(1);
        cell.setCellValue("Mã học sinh");
        cell.setCellStyle(styleHeader);

        cell = row.createCell(2);
        cell.setCellValue("Tên học sinh");
        cell.setCellStyle(styleHeader);


        cell = row.createCell(3);
        cell.setCellValue("Điểm hệ số 1");
        cell.setCellStyle(styleHeader);

        cell = row.createCell(4);
        cell.setCellValue("Điểm hệ số 2");
        cell.setCellStyle(styleHeader);

        cell = row.createCell(5);
        cell.setCellValue("Điểm hệ số 3");
        cell.setCellStyle(styleHeader);

        cell = row.createCell(6);
        cell.setCellValue("Điểm trung bình");
        cell.setCellStyle(styleHeader);

        CellStyle styleCell = workbook.createCellStyle();
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);

        List<PointDTO> lstPoint = getPoint(classId, subjectId, sem);
        int index = 1;
        for (PointDTO _item : lstPoint) {
            row = sheet.createRow(++rowNum);

            cell = row.createCell(0);
            cell.setCellValue(index++);
            cell.setCellStyle(styleCell);

            cell = row.createCell(1);
            cell.setCellValue(_item.getStudentCode());
            cell.setCellStyle(styleCell);

            cell = row.createCell(2);
            cell.setCellValue(_item.getStudentName());
            cell.setCellStyle(styleCell);

            cell = row.createCell(3);
            cell.setCellValue(_item.getPointMulti1());
            cell.setCellStyle(styleCell);

            cell = row.createCell(4);
            cell.setCellValue(_item.getPointMulti2());
            cell.setCellStyle(styleCell);

            cell = row.createCell(5);
            cell.setCellValue(_item.getPointMulti3());
            cell.setCellStyle(styleCell);
        }

        sheet.addMergedRegion(new CellRangeAddress(++rowNum, rowNum, 0, 6));
        row = sheet.createRow(rowNum);
        font = workbook.createFont();
        font.setBold(true);
        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("code4fun.site");
        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        if (!file.exists()) file.mkdirs();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        file = new java.io.File(path + df.format(new Date()) + ".xlsx");
        file.createNewFile();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        return "/resources/excel/" + file.getName();
    }

    public List<User> getListTeacher(Long classId){
        Optional<Classes> clazz = classRepository.findById(classId);
        List<User> lstRes = new ArrayList<>();
        if (clazz.isPresent()) {
            User u = clazz.get().getOwner();
            u.setFullName(u.getFullName() + " - GV chủ nhiệm");
            lstRes.add(u);
        }
        lstRes.addAll(jLessionRepository.findUserByClassId(classId));
        return lstRes;
    }
}
