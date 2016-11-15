<?xml version="1.0" encoding="utf-8"?>


<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

<fo:layout-master-set>
    <!-- layout information -->
    <fo:simple-page-master master-name="simple"
                           page-height="148mm"
                           page-width="105mm"
                           margin-top="0.6cm"
                           margin-bottom="0.6cm"
                           margin-left="0.9cm"
                           margin-right="0.9cm">
        <fo:region-body padding-before="0.3cm" border-right-style="solid"
                        border-right-width="0.7mm" border-left-style="solid"
                        border-left-width="0.7mm"/>
        <fo:region-before extent="2.5cm" border-top-style="solid"
                          border-top-width="0.7mm"/>
        <fo:region-after extent="2.4cm" border-bottom-style="solid"
                         border-bottom-width="0.7mm"/>
        <fo:region-start extent="0cm"/>
        <fo:region-end extent="0cm"/>
    </fo:simple-page-master>
</fo:layout-master-set>
<!-- end: defines page layout -->


<!--PAGE #1-->
<fo:page-sequence master-reference="simple">
    <fo:flow flow-name="xsl-region-body">

        <fo:table table-layout="fixed" width="90%" start-indent="0.25cm">
            <fo:table-column column-width="6.1cm"/>
            <fo:table-column column-width="1.9cm"/>
            <fo:table-body start-indent="0cm">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11px"
                                  font-family="Helvetica" font-weight="bold">
                        Demo,
                            <fo:block/>
                        Demo
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-size="8px" font-family="Helvetica">
                            READY BY
                        </fo:block>
                        <fo:block font-size="16px" font-family="Helvetica" font-weight="bold" text-align="center"
                                  border-style="solid" border-width="0.7mm">
                        Sat
                            <fo:block/>
                        07/20
                            <fo:block font-size="12px">
                            12:00 AM
                            </fo:block>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>

        </fo:table>

        <fo:table table-layout="fixed" width="90%" space-before="4mm" start-indent="3mm">
            <fo:table-column column-width="0.5cm"/>
            <fo:table-column column-width="7.5cm"/>
            <fo:table-body start-indent="0mm">
                <fo:table-row line-height="0.8cm">
                    <fo:table-cell number-columns-spanned="2" border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="14px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                            #demo - Demo,D.
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row line-height="6mm">
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="center" font-size="9px"
                                  font-family="Helvetica" font-weight="bold">
                        1
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="9px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                        Pants
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>
                <fo:table-row line-height="6mm">
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="center" font-size="9px"
                                  font-family="Helvetica" font-weight="bold">
                        2
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="9px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                        Pants
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>
            </fo:table-body>

        </fo:table>

        <fo:block-container width="7cm" font-size="9px" space-before="4mm"
                            font-family="Helvetica">
            <fo:block start-indent="3mm">
                <fo:inline font-weight="bold">Disclaimer:</fo:inline>
                Men’s Wearhouse is not responsible for items left in store for over 60 days or for items left in
                pockets. Liability limited to cost of alteration services provided.
            </fo:block>
        </fo:block-container>

    </fo:flow>
</fo:page-sequence>

<!--PAGE #2-->
<fo:page-sequence master-reference="simple">
    <fo:static-content flow-name="xsl-region-after">
        <fo:block
                font-size="9px"
                font-weight="bold"
                font-family="Helvetica" text-align="right" margin-right="0.4cm">
        DEMO
        </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

        <fo:table table-layout="fixed" width="90%" start-indent="2.5mm">
            <fo:table-column column-width="6.05cm"/>
            <fo:table-column column-width="1.9cm"/>
            <fo:table-body start-indent="0mm">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11px"
                                  font-family="Helvetica" font-weight="bold">
                        Demo,
                            <fo:block/>
                        Demo
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-size="8px" font-family="Helvetica">
                            READY BY
                        </fo:block>
                        <fo:block font-size="16px" font-family="Helvetica" font-weight="bold" text-align="center"
                                  border-style="solid" border-width="0.7mm">
                        Sat
                            <fo:block/>
                        07/20
                            <fo:block font-size="12px">
                            12:00 AM
                            </fo:block>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>

        </fo:table>

        <fo:table table-layout="fixed" width="90%" space-before="4mm" start-indent="3mm">
            <fo:table-column column-width="0.5cm"/>
            <fo:table-column column-width="6.7cm"/>
            <fo:table-column column-width="0.75cm"/>
            <fo:table-body start-indent="0mm">
                <fo:table-row line-height="0.8cm">
                    <fo:table-cell number-columns-spanned="3" border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="14px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                            #demo - Demo,D.
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row line-height="6mm">
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="center" font-size="9px"
                                  font-family="Helvetica" font-weight="bold">
                        1
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="9px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                        Pants

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="right" font-size="9px"
                                  font-family="Helvetica" start-indent="1mm">
                        15
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>
                <fo:table-row line-height="6mm">
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="center" font-size="9px"
                                  font-family="Helvetica" font-weight="bold">
                        2
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="left" font-size="9px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                        Pants

                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="right" font-size="9px"
                                  font-family="Helvetica" start-indent="1mm">
                        0
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>
                <fo:table-row line-height="6mm">
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block/>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block/>
                    </fo:table-cell>
                    <fo:table-cell border-style="solid"
                                   border-width="0.2mm">
                        <fo:block text-align="right" font-size="9px"
                                  font-family="Helvetica" font-weight="bold" start-indent="1mm">
                        15
                        </fo:block>
                    </fo:table-cell>

                </fo:table-row>
            </fo:table-body>

        </fo:table>


    </fo:flow>
</fo:page-sequence>

<!--PAGE #3-->
<fo:page-sequence master-reference="simple">
    <fo:static-content flow-name="xsl-region-after">
        <fo:block
                font-size="9px"
                font-weight="bold"
                font-family="Helvetica" text-align="right" margin-right="0.7cm" line-height="0.5">
        DEMO
        </fo:block>
        <fo:block text-align="right">
            <fo:instream-foreign-object padding-right="0.2cm">
                <bc:barcode xmlns:bc="http://barcode4j.krysalis.org/ns"
                            message="test">
                    <bc:qr>
                        <module-width>0.8mm</module-width>
                    </bc:qr>

                </bc:barcode>
            </fo:instream-foreign-object>
        </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

        <fo:table table-layout="fixed" width="90%" start-indent="0.25cm">
            <fo:table-column column-width="6.1cm"/>
            <fo:table-column column-width="1.9cm"/>
            <fo:table-body start-indent="0cm">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11px"
                                  font-family="Helvetica" font-weight="bold">
                        Demo,
                            <fo:block/>
                        Demo
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-size="8px" font-family="Helvetica">
                            READY BY
                        </fo:block>
                        <fo:block font-size="16px" font-family="Helvetica" font-weight="bold" text-align="center"
                                  border-style="solid" border-width="0.7mm">
                        Sat
                            <fo:block/>
                        07/20
                            <fo:block font-size="12px">
                            12:00 AM
                            </fo:block>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell padding-before="0.3cm">

                        <fo:block>
                            <fo:table table-layout="fixed" width="65%" space-before="4mm">
                                <fo:table-column column-width="5.2cm"/>
                                <fo:table-column column-width="0.5cm"/>
                                <fo:table-body>
                                    <fo:table-row line-height="0.8cm">
                                        <fo:table-cell number-columns-spanned="2" border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block text-align="left" font-size="12px"
                                                      font-family="Helvetica" font-weight="bold" start-indent="1mm">
                                                #demo – Pants
                                                    (SINGLE)
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                            <fo:table-row line-height="6mm">
                                                <fo:table-cell border-style="solid"
                                                               border-width="0.2mm">
                                                    <fo:block text-align="left" font-size="9px"
                                                              font-family="Helvetica">
                                                    Shorten Sleeves
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border-style="solid"
                                                               border-width="0.2mm">
                                                    <fo:block text-align="right" font-size="9px"
                                                              font-family="Helvetica" start-indent="1mm">
                                                    15
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                    <fo:table-row line-height="6mm">
                                        <fo:table-cell border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block/>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block text-align="right" font-size="9px"
                                                      font-family="Helvetica" font-weight="bold" start-indent="1mm">
                                            15
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>

                                </fo:table-body>

                            </fo:table>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell padding-before="0.3cm">

                        <fo:block font-family="Helvetica" border-style="solid" border-width="0.4mm" space-before="0.5cm"
                                  text-align="center"
                                  line-height="0.95cm">
                            <fo:inline font-weight="bold">1</fo:inline>
                            of
                            <fo:inline font-weight="bold">2</fo:inline>
                        </fo:block>

                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>

        </fo:table>


    </fo:flow>
</fo:page-sequence>
<fo:page-sequence master-reference="simple">
    <fo:static-content flow-name="xsl-region-after">
        <fo:block
                font-size="9px"
                font-weight="bold"
                font-family="Helvetica" text-align="right" margin-right="0.7cm" line-height="0.5">
        DEMO
        </fo:block>
        <fo:block text-align="right">
            <fo:instream-foreign-object padding-right="0.2cm">
                <bc:barcode xmlns:bc="http://barcode4j.krysalis.org/ns"
                            message="test1">
                    <bc:qr>
                        <module-width>0.8mm</module-width>
                    </bc:qr>

                </bc:barcode>
            </fo:instream-foreign-object>
        </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

        <fo:table table-layout="fixed" width="90%" start-indent="0.25cm">
            <fo:table-column column-width="6.1cm"/>
            <fo:table-column column-width="1.9cm"/>
            <fo:table-body start-indent="0cm">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11px"
                                  font-family="Helvetica" font-weight="bold">
                        Demo,
                            <fo:block/>
                        Demo
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-size="8px" font-family="Helvetica">
                            READY BY
                        </fo:block>
                        <fo:block font-size="16px" font-family="Helvetica" font-weight="bold" text-align="center"
                                  border-style="solid" border-width="0.7mm">
                        Sat
                            <fo:block/>
                        07/20
                            <fo:block font-size="12px">
                            12:00 AM
                            </fo:block>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell padding-before="0.3cm">

                        <fo:block>
                            <fo:table table-layout="fixed" width="65%" space-before="4mm">
                                <fo:table-column column-width="5.2cm"/>
                                <fo:table-column column-width="0.5cm"/>
                                <fo:table-body>
                                    <fo:table-row line-height="0.8cm">
                                        <fo:table-cell number-columns-spanned="2" border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block text-align="left" font-size="12px"
                                                      font-family="Helvetica" font-weight="bold" start-indent="1mm">
                                                #demo – Pants
                                                    (SINGLE)
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>

                                    <fo:table-row line-height="6mm">
                                        <fo:table-cell border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block/>
                                        </fo:table-cell>
                                        <fo:table-cell border-style="solid"
                                                       border-width="0.2mm">
                                            <fo:block text-align="right" font-size="9px"
                                                      font-family="Helvetica" font-weight="bold" start-indent="1mm">
                                            0
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>

                                </fo:table-body>

                            </fo:table>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell padding-before="0.3cm">

                        <fo:block font-family="Helvetica" border-style="solid" border-width="0.4mm" space-before="0.5cm"
                                  text-align="center"
                                  line-height="0.95cm">
                            <fo:inline font-weight="bold">2</fo:inline>
                            of
                            <fo:inline font-weight="bold">2</fo:inline>
                        </fo:block>

                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>

        </fo:table>


    </fo:flow>
</fo:page-sequence>
</fo:root>
