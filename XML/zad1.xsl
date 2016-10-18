<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
    <h2>My People Collection</h2>
    <table border="1">
      <tr bgcolor="#FF0000">
      <th>Imie</th>
      <th>Nazwisko</th>
      <th>Wiek</th>
      <th>Adres</th>
    </tr>
    <xsl:for-each select="people/person">
    <xsl:if test="age>25">
      <tr bgcolor="#aa9988">
        <td><xsl:value-of select="name"/></td>
        <td><xsl:value-of select="name2"/></td>
        <td><xsl:value-of select="age"/></td> 
        <td><xsl:value-of select="address/street"/>&#160;<xsl:value-of select="address/nr"/></td>
      </tr>
    </xsl:if>
    </xsl:for-each>
      <tr bgcolor="#7777FF">
        <td>N/A</td>
        <td>N/A</td>
	    <td><xsl:value-of select='sum(//people/person/age) div count(//people/person/age)'/></td>
        <td>N/A</td>
      </tr>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>