from pathlib import Path
from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT, TA_CENTER
from reportlab.lib.pagesizes import LETTER
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, ListFlowable, ListItem, PageBreak, Table, TableStyle

OUTPUT_PATH = Path('/Users/shashaankreddy/Documents/Newjobs/Shashaank_Gurrala_Resume.pdf')
OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name='ResumeTitle', parent=styles['Title'], fontName='Helvetica-Bold', fontSize=24, leading=28, textColor=colors.HexColor('#0F172A'), alignment=TA_LEFT))
styles.add(ParagraphStyle(name='ResumeSubtitle', parent=styles['Heading2'], fontName='Helvetica', fontSize=12, leading=16, textColor=colors.HexColor('#475569'), alignment=TA_LEFT))
styles.add(ParagraphStyle(name='SectionTitle', parent=styles['Heading2'], fontName='Helvetica-Bold', fontSize=12.5, leading=14, textColor=colors.HexColor('#2563EB'), spaceAfter=6, borderWidth=0))
styles.add(ParagraphStyle(name='Body', parent=styles['BodyText'], fontName='Helvetica', fontSize=10, leading=13.5, textColor=colors.HexColor('#1F2937')))
styles.add(ParagraphStyle(name='BodyBold', parent=styles['BodyText'], fontName='Helvetica-Bold', fontSize=10, leading=13.5, textColor=colors.HexColor('#111827')))
styles.add(ParagraphStyle(name='Meta', parent=styles['BodyText'], fontName='Helvetica', fontSize=9.5, leading=13, textColor=colors.HexColor('#64748B')))


def bullet_list(items):
    return ListFlowable([
        ListItem(Paragraph(item, styles['Body']), value='\u2022')
        for item in items
    ], bulletType='bullet', bulletColor=colors.HexColor('#2563EB'))


def section(title, content):
    return [Paragraph(title, styles['SectionTitle']), *content, Spacer(1, 0.12 * inch)]


def build_story():
    story = []

    header = Table(
        [[Paragraph('Shashaank Reddy Gurrala', styles['ResumeTitle']), Paragraph('Data Engineer | Azure, Databricks & Cloud Data Engineering', styles['ResumeSubtitle'])]],
        colWidths=[3.8 * inch, 3.2 * inch],
        rowHeights=[0.7 * inch],
        style=[('BACKGROUND', (0, 0), (-1, -1), colors.HexColor('#EFF6FF')), ('TEXTCOLOR', (0, 0), (-1, -1), colors.HexColor('#0F172A')), ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'), ('LEFTPADDING', (0, 0), (-1, -1), 18), ('RIGHTPADDING', (0, 0), (-1, -1), 18), ('BOTTOMPADDING', (0, 0), (-1, -1), 8) ]
    )
    story.append(header)
    story.append(Spacer(1, 0.12 * inch))

    contact = Paragraph('shashaankreddy01@gmail.com • +1 (309) 831-9147 • linkedin.com/in/shashaank-reddy-g', styles['Meta'])
    story.append(contact)
    story.append(Spacer(1, 0.16 * inch))

    story.extend(section('PROFESSIONAL SUMMARY', [
        Paragraph('Data Engineer with 5+ years of experience designing and scaling cloud-native data platforms for healthcare and financial services. Known for building robust ETL pipelines on Azure and Databricks, strengthening data governance, and delivering measurable performance gains while supporting HIPAA-sensitive analytics workloads.', styles['Body'])
    ]))

    story.extend(section('TECHNICAL SKILLS', [
        Paragraph('Azure: Azure Data Factory, Azure Databricks, ADLS Gen2, Azure Synapse, Event Hubs, Stream Analytics, Logic Apps, Functions, Key Vault, DevOps • Big Data: Spark, PySpark, Delta Lake, HDFS, Hive, Kafka • Databases: Snowflake, SQL Server, Azure SQL, Oracle, Cosmos DB • Languages: SQL, PL/SQL, Python, HiveQL, Scala, Java • Modeling: Star Schema, Snowflake Schema, ER/Studio, Erwin • DevOps: Git, GitHub, Azure DevOps, CI/CD, SonarQube', styles['Body'])
    ]))

    story.extend(section('PROFESSIONAL EXPERIENCE', [
        Paragraph('Optum — Data Engineer', styles['BodyBold']),
        Paragraph('June 2024 – Present • Remote (San Antonio, Texas)', styles['Meta']),
        bullet_list([
            'Architected and scaled multi-client ingestion pipelines across Azure Data Factory, Databricks, and Snowflake to move JSON, CSV, and Parquet data into ADLS and Delta Lake with Unity Catalog RBAC for PHI-sensitive environments.',
            'Re-engineered the core ETL workflow with CDC and SCD Type 1/2 patterns, reducing daily processing time by 40% and enabling low-latency analytics for a risk-adjustment platform serving 17 major U.S. health plans.',
            'Delivered two production PySpark analytics frameworks using AI-assisted development, accelerating stakeholder reporting and tightening validation through automated QA and SonarQube compliance.',
            'Consolidated the platform onto Microsoft Fabric and introduced Databricks Genie to empower business users with self-service analytics on curated Gold-layer datasets.'
        ]),
        Spacer(1, 0.08 * inch),
        Paragraph('Capital One — Data Engineer', styles['BodyBold']),
        Paragraph('January 2023 – May 2024 • Remote (Normal, Illinois)', styles['Meta']),
        bullet_list([
            'Led a large-scale migration of tens of terabytes of data from on-prem SQL Server, MongoDB, Cassandra, and Oracle into ADLS Gen2 and Azure Synapse using ADF and Self-Hosted Integration Runtime.',
            'Modernized legacy Oracle ETL into cloud-native Python, PySpark, and PL/SQL pipelines with Databricks-based validation to maintain consistency across all migrated sources.',
            'Implemented Delta Lake and CDC/SCD patterns to improve reliability, reduce reprocessing for late-arriving data, and support near real-time reporting.',
            'Optimized Snowflake warehouse configuration and data modeling for enterprise BI and Power BI performance, while improving auditability through Time Travel and governance controls.'
        ]),
        Spacer(1, 0.08 * inch),
        Paragraph('OpenText — Data Engineer', styles['BodyBold']),
        Paragraph('June 2020 – December 2021 • Chennai, India', styles['Meta']),
        bullet_list([
            'Engineered B2B EDI ingestion pipelines for X12 850, 810, and 856 transactions, transforming purchase orders, invoices, and shipment notices into structured Parquet for downstream reconciliation.',
            'Automated file-arrival monitoring and pipeline triggering with Python and Azure Blob Storage SDKs, eliminating hours of manual intervention each week.',
            'Built Snowflake-based SCD Type 2 pipelines to track historical changes in supplier and product master data, enabling vendor and inventory analytics.',
            'Developed real-time event ingestion and API integration workflows that improved data freshness, compliance auditing, and partner data automation.'
        ])
    ]))

    story.extend(section('EDUCATION', [
        Paragraph('Master of Science, Computer Science and Engineering — Illinois State University', styles['BodyBold']),
        Paragraph('Jan 2022 – Dec 2023', styles['Meta']),
        Paragraph('Bachelor of Technology, Computer Science Engineering (Data Analytics) — Veltech University', styles['BodyBold']),
        Paragraph('Jun 2017 – May 2021', styles['Meta'])
    ]))

    story.extend(section('CERTIFICATIONS', [
        Paragraph('Databricks Academy Accreditation — Generative AI Fundamentals', styles['Body']),
        Paragraph('Generative AI for Everyone — DeepLearning.AI', styles['Body'])
    ]))

    return story


def main():
    doc = SimpleDocTemplate(str(OUTPUT_PATH), pagesize=LETTER, rightMargin=0.55 * inch, leftMargin=0.55 * inch, topMargin=0.45 * inch, bottomMargin=0.45 * inch)
    story = build_story()
    doc.build(story)
    print(f'Created resume PDF at {OUTPUT_PATH}')


if __name__ == '__main__':
    main()
