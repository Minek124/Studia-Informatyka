<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
    <h2>Wzory znalezione w wikipedii</h2>
    <table border="1">
      <tr bgcolor="#FF0000">
        <th>Wzor</th>
        <th>Kod</th>
      </tr>
      <xsl:for-each select='//*[@class="mwe-math-fallback-image-inline tex"]'>
        <tr bgcolor="#aa9988">
          <td><img><xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute></img></td>
          <td><xsl:value-of select="@alt"/></td>
        </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
