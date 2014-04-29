package ch.jtaf.report;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import ch.jtaf.data.SeriesRankingCategoryData;
import ch.jtaf.data.SeriesRankingData;
import ch.jtaf.i18n.I18n;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import org.jboss.logging.Logger;

public class SeriesRanking extends Ranking {

    private Document document;
    private PdfWriter pdfWriter;
    private final SeriesRankingData ranking;
    private final Locale locale;

    public SeriesRanking(SeriesRankingData ranking, Locale locale) {
        this.ranking = ranking;
        this.locale = locale;
    }

    public byte[] create() {
        try {
            byte[] ba;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document = new Document(PageSize.A4);
                pdfWriter = PdfWriter.getInstance(document, baos);
                pdfWriter.setPageEvent(new HeaderFooter(
                        I18n.getInstance().getString(locale, "Series Ranking"), 
                        ranking.getSeries().getName(),
                        sdf.format(new Date())));
                document.open();
                createRanking();
                document.close();
                pdfWriter.flush();
                ba = baos.toByteArray();
            }
            return ba;
        } catch (DocumentException | IOException e) {
            Logger.getLogger(SeriesRanking.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private void createRanking() throws DocumentException {
        for (SeriesRankingCategoryData category : ranking.getCategories()) {
            PdfPTable table = createAthletesTable();
            createCategoryTitle(table, category);

            int position = 1;
            for (Athlete athlete : category.getAthletes()) {
                createAthleteRow(table, position, athlete);
                position++;
                numberOfRows += 1;
                if (numberOfRows > 24) {
                    document.add(table);
                    table = createAthletesTable();
                    document.newPage();
                }
            }
            document.add(table);
            numberOfRows += 3;
        }
    }

    private PdfPTable createAthletesTable() {
        PdfPTable table = new PdfPTable(new float[]{2f, 10f, 10f, 2f, 5f, 5f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(cmToPixel(1f));
        return table;
    }

    private void createCategoryTitle(PdfPTable table, SeriesRankingCategoryData category) {
        addCategoryTitleCellWithColspan(table, category.getCategory().getAbbreviation(), 1);
        addCategoryTitleCellWithColspan(table, category.getCategory().getName() + " "
                + category.getCategory().getYearFrom() + " - " + category.getCategory().getYearTo(), 5);

        addCategoryTitleCellWithColspan(table, " ", 6);
    }

    private void createAthleteRow(PdfPTable table, int position, Athlete athlete) throws DocumentException {
        addCell(table, position + ".");
        addCell(table, athlete.getLastName());
        addCell(table, athlete.getFirstName());
        addCell(table, athlete.getYear() + "");
        addCell(table, athlete.getClub() == null ? "" : athlete.getClub().getAbbreviation());
        addCellAlignRight(table, athlete.getSeriesPoints(ranking.getSeries()) + "");

        StringBuilder sb = new StringBuilder();
        for (Competition competition : ranking.getSeries().getCompetitions()) {
            sb.append(competition.getName());
            sb.append(": ");
            sb.append(athlete.getTotalPoints(competition));
            sb.append(" ");
        }
        addCell(table, "");
        addResultsCell(table, sb.toString());
    }
}
