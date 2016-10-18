<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes"/>
<xsl:template match="/">
  <html>
  <body>
    <h1>Contents of the DB with <xsl:value-of select="count(//person)"/> people</h1>
    <table border="1">
      <tr>
      <th>First Name</th>
      <th>Last Name</th>
      <th>Number</th>
    </tr>
    <xsl:for-each select="//person">
      <xsl:sort select="first"/>
      <tr>
        <td><xsl:value-of select="first"/></td>
        <td><xsl:value-of select="last"/></td>
        <td>(<xsl:value-of select="@idnum"/>)</td>
      </tr>
    </xsl:for-each>
      
    </table>
  </body>
  </html>
</xsl:template>
</xsl:transform>