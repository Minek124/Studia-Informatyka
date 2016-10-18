#!/usr/bin/python
import xml.sax
 
class ABContentHandler(xml.sax.ContentHandler):
  def __init__(self):
    xml.sax.ContentHandler.__init__(self)
 
  def startElement(self, name, attrs):
    print("<" + name + ">"),
    for i in attrs.getNames():
      print(attrs.getValue(i) + ", "),
    
  def endElement(self, name):   
    print("</" + name + ">")
 
  def characters(self, content):
    print(content),
 
def main(sourceFileName):
  source = open(sourceFileName)
  xml.sax.parse(source, ABContentHandler())
 
if __name__ == "__main__":
  main("recipes.xml")