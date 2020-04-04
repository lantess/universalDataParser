package XML;

import java.util.HashMap;
import java.util.Map;

public class XMLTag {
    private String _tagName,
                    _value;
    private HashMap<String, String> _attr;

    public XMLTag(){
        this("","");
    }
    public XMLTag(String tagName){
        this(tagName,"");
    }
    public XMLTag(String tagName, String value){
        this._tagName = tagName;
        this._value = value;
        this._attr = new HashMap<>();
    }
    public XMLTag(String tagName, String value, String attr) throws XMLSyntaxError{
        this(tagName, value);
        _attr = new AttributeProcessor(attr).toHashMap();
    }

    public String getTagName(){
        return _tagName;
    }
    public String getValue(){
        return _value;
    }
    public boolean hasAttribute(String key){
        return _attr.containsKey(key);
    }
    public String getAttribute(String key){
        return _attr.get(key);
    }


    @Override
    public String toString(){
        StringBuilder res = new StringBuilder("<");
        res.append(_tagName).append(" ");
        for(Map.Entry<String,String> e : _attr.entrySet())
            if(e.getValue() != null)
                res.append(e.getKey()).append("=\"").append(e.getValue()).append("\" ");
            else
                res.append(e.getKey()).append(" ");
        res.append(">").append(_value).append("</").append(_tagName).append(">");
        return res.toString();
    }

    private class AttributeProcessor{
        private final char QUOTE = '\"',
                            APOSTROPHE = '\'',
                            EQUAL = '=',
                            SPACE = ' ';
        private HashMap<String, String> _data;

        public AttributeProcessor(String attr) throws XMLSyntaxError{
            _data = new HashMap<>();
            while(attr.length()>0){
                int eqPos = attr.indexOf(EQUAL),
                        spacePos = attr.indexOf(SPACE);
                if(eqPos!=-1)
                    if(eqPos<spacePos)
                        attr = processAttr(attr, eqPos);
                    else{
                        _data.put(attr.substring(0, spacePos), null);
                        attr = attr.substring(spacePos+1);
                    }
                else {
                    _data.put(attr.trim(), null);
                    attr = "";
                }
            }
        }
        private String processAttr(String attr, int eqPos) throws XMLSyntaxError{
            String key = attr.substring(0, eqPos).trim();
            attr = attr.substring(eqPos+1).trim();
            boolean isQuote = attr.charAt(0) == QUOTE,
                    isApostrophe = attr.charAt(0) == APOSTROPHE;
            int begin = isApostrophe || isQuote ? 1 : 0,
                    end = isQuote ? getPosOfApostropheOrQuote(attr, QUOTE) :
                            isApostrophe ? getPosOfApostropheOrQuote(attr, APOSTROPHE) :
                                    getPosOfSpaceOrNull(attr);
            String value = attr.substring(begin,end);
            _data.put(key.trim(), value.trim());
            return attr.substring(end+1);
        }
        private int getPosOfApostropheOrQuote(String s, char c) throws XMLSyntaxError{
            int res = s.substring(1)
                    .indexOf(c);
            if(res==-1)
                throw new XMLSyntaxError("SYNTAX ERROR: '\"' or ''' missing.");
            return res+1;
        }
        private int getPosOfSpaceOrNull(String s){
            int res = s.indexOf(" ");
            return res == -1 ? s.length() : res;
        }

        public HashMap<String, String> toHashMap(){
            return _data;
        }
    }

}
