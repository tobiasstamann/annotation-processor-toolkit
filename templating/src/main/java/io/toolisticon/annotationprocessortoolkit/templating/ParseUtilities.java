package io.toolisticon.annotationprocessortoolkit.templating;

import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.ForTemplateBlock;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.IfTemplateBlock;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.IncludeTemplateBlock;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.PlainTextTemplateBlock;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.StaticTemplateBlock;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.TemplateBlockBinder;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.TemplateBlockType;
import io.toolisticon.annotationprocessortoolkit.templating.templateblocks.VariableTextTemplateBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseUtilities {

    /**
     * Result class to store a parsing result.
     */
    public static class ParserResult {

        private final int beginIndex;
        private final int endIndex;
        private final String content;

        public ParserResult(int beginIndex, int endIndex, String content) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.content = content;
        }


        public int getBeginIndex() {
            return beginIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public String getContent() {
            return content;
        }
    }


    /**
     * Enum that defines type of the next block
     */
    private enum NextBlockType {
        NONE,
        FOR,
        IF,
        STATIC,
        DYNAMIC_TEXT
    }


    public static final Pattern DYNAMIC_TEXT_BLOCK_REGEX = Pattern.compile("[$][{]\\s*((?:\\w|.)*?)\\s*[}]");


    public static TemplateBlockBinder parseString(String templateString) {

        String tmpTemplateString = templateString;

        TemplateBlockBinder binder = new TemplateBlockBinder(tmpTemplateString);

        NextDetectedBlockResult nextBlock = TemplateBlockType.getNextBlock(templateString);

        while (nextBlock != null) {

            // add plain text block upfront
            if (nextBlock.getBeginIndex() != 0) {
                binder.addTemplateBlock(new PlainTextTemplateBlock(tmpTemplateString.substring(0, nextBlock.getBeginIndex())));
            }


            switch (nextBlock.getTemplateBlockType()) {

                case DYNAMIC_TEXT: {
                    ParserResult nextDynamicText = getNextDynamicText(tmpTemplateString);
                    binder.addTemplateBlock(new VariableTextTemplateBlock(nextDynamicText.getContent()));
                    tmpTemplateString = tmpTemplateString.substring(nextDynamicText.getEndIndex());
                    break;
                }
                case FOR: {


                    ForTemplateBlock forTemplateBlock = new ForTemplateBlock(nextBlock.getAttributes(), nextBlock.getContent());
                    binder.addTemplateBlock(forTemplateBlock);

                    forTemplateBlock.setBinder(parseString(forTemplateBlock.getTemplateString()));
                    tmpTemplateString = nextBlock.getRemainingStringToBeProcessed();

                    break;
                }
                case IF: {

                    IfTemplateBlock ifTemplateBlock = new IfTemplateBlock(nextBlock.getAttributes(), nextBlock.getContent());
                    binder.addTemplateBlock(ifTemplateBlock);

                    ifTemplateBlock.setBinder(parseString(ifTemplateBlock.getTemplateString()));
                    tmpTemplateString = nextBlock.getRemainingStringToBeProcessed();

                    break;
                }
                case INCLUDE: {
                    IncludeTemplateBlock includeTemplateBlock = new IncludeTemplateBlock(nextBlock.getAttributes());
                    binder.addTemplateBlock(includeTemplateBlock);

                    tmpTemplateString = nextBlock.getRemainingStringToBeProcessed();

                    break;
                }
                case STATIC: {


                    StaticTemplateBlock staticTemplateBlock = new StaticTemplateBlock(nextBlock.getContent());
                    binder.addTemplateBlock(staticTemplateBlock);

                    tmpTemplateString = nextBlock.getRemainingStringToBeProcessed();


                    break;
                }


            }

            // get next block type
            nextBlock = TemplateBlockType.getNextBlock(tmpTemplateString);
        }

        // add last plain text block
        binder.addTemplateBlock(new PlainTextTemplateBlock(tmpTemplateString));

        return binder;
    }


    /**
     * Get dynamic text block.
     *
     * @param templateString the template string to parse
     * @return the ParseResult
     */
    protected static ParserResult getNextDynamicText(String templateString) {

        if (templateString == null) {
            return null;
        }

        // first check for control block sequence
        Matcher matcher = DYNAMIC_TEXT_BLOCK_REGEX.matcher(templateString);
        if (matcher.find()) {

            return new ParserResult(matcher.start(), matcher.end(), matcher.group(1));

        }

        return null;
    }


    /**
     * Reads a resource file into a String
     *
     * @param resourcefileName
     * @return the content of the resource file as a String
     * @throws IOException
     */
    public static String readResourceToString(String resourcefileName) throws IOException {

        InputStream inputStream = ParseUtilities.class.getResourceAsStream(resourcefileName);
        if (inputStream != null) {
            return readFromInputStream(inputStream);
        } else {
            throw new IllegalArgumentException("Can't open resource file '" + resourcefileName + "'");
        }


    }

    /**
     * Reads a String from an InputStream.
     * CLoses the stream
     *
     * @param stream the inputStream to use
     * @return The String read from the inputStreams
     * @throws IOException
     */
    public static String readFromInputStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[10000];
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int line = 0;
        // read bytes from stream, and store them in buffer
        while ((line = stream.read(buffer)) != -1) {
            // Writes bytes from byte array (buffer) into output stream.
            os.write(buffer, 0, line);
        }
        stream.close();
        os.flush();
        os.close();

        return new String(os.toByteArray());
    }


    /**
     * Trims content string.
     * Allows better formatting of templates.
     * Removes all leading whitespaces
     * Remove trailing spaces up to last newline (remove newline then too ) or non space char
     *
     * @param content
     * @return
     */
    public static String trimContentString(String content) {

        String tmpContentString = content;
        tmpContentString = tmpContentString.replaceFirst("^[ ]*?\n", "");
        tmpContentString = tmpContentString.replaceAll("[ ]+$", "");
        return tmpContentString;

    }

    /**
     * Pareses a string and creates a map containing attribute names and values.
     * If an attribute is set more than once, the last attribute value will be included.
     *
     * @param attributeString the string to parse
     * @return a map containing all key value pairs
     */
    public static Map<String, String> parseNamedAttributes(String attributeString) {

        Map<String, String> attributeMap = new HashMap<>();
        final String attributePatternString = "\\s*(\\w+)\\s*:\\s*'(.*?)'\\s*";

        Pattern attributePattern = Pattern.compile("(" + attributePatternString + ")(?:,(\" + attributePatternString+ \"))*");

        Matcher matcher = attributePattern.matcher(attributeString);
        if (matcher.matches()) {

            Pattern findPattern = Pattern.compile(attributePatternString);
            Matcher findMatcher = findPattern.matcher(attributeString);

            while (findMatcher.find()) {
                String attributeName = findMatcher.group(1);
                String attributeValue = findMatcher.group(2);

                attributeMap.put(attributeName, attributeValue);
            }

        }
        return attributeMap;
    }

}
