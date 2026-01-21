import os
import shutil
from fpdf import FPDF

class AcademicReport(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font('helvetica', 'I', 8)
            self.cell(0, 10, self.clean_text('Application Web de Gestion d\'un Centre de Formation - Rapport Academique'), 0, 1, 'R')
            self.ln(5)

    def footer(self):
        self.set_y(-15)
        self.set_font('helvetica', 'I', 8)
        self.cell(0, 10, f'Page {self.page_no()}', 0, 0, 'C')

    def clean_text(self, text):
        return text.encode('latin-1', 'replace').decode('latin-1')

    def chapter_title(self, num, label):
        self.set_font('helvetica', 'B', 16)
        self.set_text_color(20, 40, 100)
        self.cell(0, 10, self.clean_text(f'{num}. {label}'), 0, 1, 'L')
        self.ln(4)
        self.set_text_color(0, 0, 0)

    def chapter_body(self, body):
        self.set_font('helvetica', '', 12)
        self.multi_cell(0, 7, self.clean_text(body))
        self.ln()

    def add_image_with_caption(self, image_path, caption, w=150):
        if os.path.exists(image_path):
            x = (self.w - w) / 2
            self.image(image_path, x=x, w=w)
            self.ln(2)
            self.set_font('helvetica', 'I', 10)
            self.cell(0, 10, self.clean_text(f'Figure: {caption}'), 0, 1, 'C')
            self.ln(5)
        else:
            self.set_font('helvetica', 'B', 10)
            self.set_text_color(255, 0, 0)
            self.cell(0, 10, self.clean_text(f'[Image manquante: {caption}]'), 0, 1, 'C')
            self.set_text_color(0, 0, 0)
            self.ln(5)

def generate_pdf():
    pdf = AcademicReport()
    pdf.set_auto_page_break(auto=True, margin=15)
    
    # --- Page de Garde ---
    pdf.add_page()
    pdf.set_font('helvetica', 'B', 24)
    pdf.ln(40)
    pdf.cell(0, 20, pdf.clean_text('RAPPORT DE MINI-PROJET'), 0, 1, 'C')
    pdf.ln(10)
    pdf.set_font('helvetica', 'B', 18)
    pdf.cell(0, 15, pdf.clean_text('Application Web de Gestion d\'un Centre de Formation'), 0, 1, 'C')
    pdf.ln(20)
    pdf.set_font('helvetica', '', 14)
    pdf.cell(0, 10, pdf.clean_text('Realise dans le cadre de la formation d\'Ingenieur'), 0, 1, 'C')
    pdf.ln(40)
    pdf.set_font('helvetica', 'B', 12)
    pdf.cell(0, 10, pdf.clean_text('Presente par: [Votre Nom]'), 0, 1, 'C')
    pdf.cell(0, 10, pdf.clean_text('Encadre par: [Nom de l\'encadrant]'), 0, 1, 'C')
    pdf.ln(40)
    pdf.cell(0, 10, pdf.clean_text('Annee Universitaire: 2025-2026'), 0, 1, 'C')
    
    # --- Table des matieres ---
    pdf.add_page()
    pdf.set_font('helvetica', 'B', 16)
    pdf.cell(0, 10, pdf.clean_text('Table des Matieres'), 0, 1, 'L')
    pdf.ln(5)
    pdf.set_font('helvetica', '', 12)
    sections = [
        "1. Introduction",
        "2. Analyse et specification des besoins",
        "3. Architecture de la solution",
        "4. Conception UML",
        "5. Implementation",
        "6. Securite et gestion des roles",
        "7. Reporting et fonctionnalites avancees",
        "8. Resultats et captures",
        "9. Conclusion et perspectives"
    ]
    for s in sections:
        pdf.cell(0, 10, pdf.clean_text(s), 0, 1, 'L')
    
    # --- 1. Introduction ---
    pdf.add_page()
    pdf.chapter_title(1, "Introduction")
    intro_txt = (
        "Le projet 'Application web de gestion d'un centre de formation' s'inscrit dans une demarche de modernisation "
        "des outils de gestion pedagogique au sein d'une ecole d'ingenieurs. L'objectif est de centraliser "
        "toutes les informations relatives au cycle de vie d'une formation, de l'inscription des etudiants "
        "jusqu'a la validation des acquis par le biais de rapports de notes.\n\n"
        "Objectifs pedagogiques :\n"
        "- Maitriser l'ecosysteme Spring Boot pour le developpement backend.\n"
        "- Comprendre l'architecture en couches et les principes de l'injection de dependances.\n"
        "- Mettre en oeuvre une securite robuste basee sur les roles.\n"
        "- Allier le rendu cote serveur (SSR) via Thymeleaf pour l'administration et le rendu cote client (CSR) pour une experience utilisateur fluide.\n\n"
        "Interet de l'architecture Spring Boot :\n"
        "Spring Boot offre une configuration simplifiee (auto-configuration), une gestion efficace de la persistance avec Spring Data JPA, "
        "et une integration facilitee de modules de securite. Sa modularite permet d'exposer des API REST tout en "
        "servant des vues classiques, repondant ainsi aux besoins hybrides du projet."
    )
    pdf.chapter_body(intro_txt)
    
    # --- 2. Analyse et specification des besoins ---
    pdf.add_page()
    pdf.chapter_title(2, "Analyse et specification des besoins")
    besoins_txt = (
        "L'application doit repondre aux besoins de trois types d'acteurs principaux :\n\n"
        "1. Administrateur (ADMIN) :\n"
        "- Gestion du catalogue de formations (cours, sessions).\n"
        "- Gestion des comptes utilisateurs (etudiants, formateurs).\n"
        "- Affectation des formateurs aux cours.\n"
        "- Surveillance globale des activites du centre.\n\n"
        "2. Formateur (FORMATEUR) :\n"
        "- Consultation de son emploi du temps.\n"
        "- Saisie des notes pour les etudiants inscrits a ses cours.\n"
        "- Gestion des supports pedagogiques.\n\n"
        "3. Etudiant (ETUDIANT) :\n"
        "- Inscription aux sessions de cours disponibles.\n"
        "- Consultation de son emploi du temps personnalise.\n"
        "- Acces a ses notes et telechargement du releve officiel.\n\n"
        "Contraintes techniques :\n"
        "- Securite JWT pour les API REST.\n"
        "- Base de donnees relationnelle MySQL.\n"
        "- Interface responsive utilisant Bootstrap.\n"
        "- Disponibilite d'un systeme de notification par email."
    )
    pdf.chapter_body(besoins_txt)
    
    # --- 3. Architecture de la solution ---
    pdf.add_page()
    pdf.chapter_title(3, "Architecture de la solution")
    arch_txt = (
        "L'application repose sur une architecture multi-couches standardisee, optimisee pour la maintenabilite et l'evolutivite.\n\n"
        "Architecture logique en couches :\n"
        "- Couche Web (Controller) : Traite les requetes HTTP, gere les entrees utilisateur et delegue les traitements a la couche service.\n"
        "- Couche Service (Business Logic) : Contient la logique metier, les validations complexes et la gestion des transactions.\n"
        "- Couche Persistance (Repository) : Interagit avec la base de donnees via Spring Data JPA.\n"
        "- Base de donnees : Stockage persistant sous MySQL.\n\n"
        "Role de Spring IoC et de l'injection de dependances :\n"
        "Le conteneur IoC (Inversion of Control) de Spring gere le cycle de vie des composants (Beans). L'injection de dependances "
        "permet de decoupler les couches. Par exemple, un contrôleur ne cree pas ses propres instances de services, "
        "mais les reçoit via son constructeur, ce qui facilite les tests unitaires.\n\n"
        "Gestion des profils (dev / prod) :\n"
        "L'utilisation des profils Spring permet de configurer des environnements distincts. En developpement, "
        "une base de donnees locale (ou H2) peut être utilisee avec une journalisation detaillee, tandis qu'en production, "
        "une instance MySQL configuree avec des parametres de securite stricts est privilegiee."
    )
    pdf.chapter_body(arch_txt)
    
    # --- 4. Conception UML ---
    pdf.add_page()
    pdf.chapter_title(4, "Conception UML")
    pdf.chapter_body("La phase de conception a permis de modeliser les interactions et les structures de donnees.")
    
    pdf.add_image_with_caption('use_case_diagram.png', "Diagramme de cas d'utilisation")
    pdf.add_page()
    pdf.add_image_with_caption('class_diagram.png', "Diagramme de classes")
    pdf.add_page()
    pdf.add_image_with_caption('sequence_diagram_enrollment.png', "Diagramme de sequence - Inscription")
    pdf.add_page()
    pdf.add_image_with_caption('component_diagram_ssr_csr.png', "Diagramme de composants")
    
    # --- 5. Implementation ---
    pdf.add_page()
    pdf.chapter_title(5, "Implementation")
    impl_txt = (
        "Technologies utilisees :\n"
        "- Backend : Spring Boot 3.4, Spring Data JPA, Hibernate, Spring Security.\n"
        "- Frontend : Thymeleaf (SSR), Bootstrap 5, Angular/React (CSR).\n"
        "- Base de donnees : MySQL 8.0.\n"
        "- Gestion de versions de BD : Liquibase.\n\n"
        "Description des entites et relations ORM :\n"
        "Le modele de donnees est centre sur l'entite User, declinee en Student et Trainer via un heritage ou des jointures. "
        "La relation entre Student et Course est geree par l'entite Enrollment, permettant de stocker la date d'inscription. "
        "Les notes (Grade) sont liees a la fois a un etudiant et a un cours.\n\n"
        "Exposition des API REST :\n"
        "Les API REST sont exposees via des RestControllers, retournant des DTO (Data Transfer Objects) "
        "pour eviter les cycles dans le JSON et securiser les transferts de donnees sensibles."
    )
    pdf.chapter_body(impl_txt)
    
    # --- 6. Securite et gestion des roles ---
    pdf.add_page()
    pdf.chapter_title(6, "Securite et gestion des roles")
    sec_txt = (
        "La securite est un pilier de l'application. Elle est implementee avec Spring Security 6.\n\n"
        "Authentification :\n"
        "Pour les parties SSR, l'authentification se fait via un formulaire classique avec gestion de session. "
        "Pour les parties CSR, un système JWT (JSON Web Token) est mis en place, permettant une authentification stateless "
        "adaptee aux clients modernes.\n\n"
        "Autorisation par roles :\n"
        "- ROLE_ADMIN : Acces total aux interfaces de gestion.\n"
        "- ROLE_FORMATEUR : Acces restreint a ses cours et a la saisie des notes.\n"
        "- ROLE_ETUDIANT : Acces a ses inscriptions et a ses resultats uniquement.\n\n"
        "Gestion du profil utilisateur :\n"
        "Chaque utilisateur peut mettre a jour ses informations personnelles et changer son mot de passe, "
        "lequel est stocke sous forme de hachage BCrypt en base de donnees."
    )
    pdf.chapter_body(sec_txt)
    
    # --- 7. Reporting et fonctionnalites avancees ---
    pdf.add_page()
    pdf.chapter_title(7, "Reporting et fonctionnalites avancees")
    adv_txt = (
        "Le systeme integre des fonctionnalites de calcul et de reporting pour faciliter le suivi pedagogique.\n\n"
        "Calcul des moyennes :\n"
        "Un service dedie parcourt les notes d'un etudiant pour calculer sa moyenne generale ponderee ou simple. "
        "Des statistiques par cours sont egalement generees (taux de reussite, ecart-type).\n\n"
        "Generation de rapports PDF :\n"
        "Grâce a iText, l'application genere dynamiquement des releves de notes au format PDF. Ces documents "
        "sont formates de maniere professionnelle, incluant le logo du centre et un recapitulatif clair des resultats.\n\n"
        "Notifications par email :\n"
        "L'integration de Spring Mail permet d'envoyer des notifications automatiques (confirmation d'inscription, "
        "alerte en cas de nouvelle note publiee, rappel de session)."
    )
    pdf.chapter_body(adv_txt)
    
    # --- 8. Resultats et captures ---
    pdf.add_page()
    pdf.chapter_title(8, "Resultats et captures")
    res_txt = (
        "Les interfaces ont ete concues pour être intuitives. La partie administration (Thymeleaf) privilegie "
        "l'efficacite operationnelle, tandis que le tableau de bord etudiant mise sur la clarte visuelle.\n\n"
        "Voici un aperçu du tableau de bord principal :"
    )
    pdf.chapter_body(res_txt)
    pdf.add_image_with_caption('dashboard_mockup.png', "Tableau de bord de gestion (SSR/Thymeleaf)")
    
    # --- 9. Conclusion et perspectives ---
    pdf.add_page()
    pdf.chapter_title(9, "Conclusion et perspectives")
    ccl_txt = (
        "Ce projet a permis de concretiser les concepts theoriques du developpement web with Spring Boot. "
        "L'application est fonctionnelle et repond aux exigences initiales de gestion d'un centre de formation.\n\n"
        "Bilan du travail realise :\n"
        "- Mise en place d'une architecture solide et securisee.\n"
        "- Developpement d'une interface hybride SSR/CSR.\n"
        "- Automatisation du reporting par PDF.\n\n"
        "Limites du projet :\n"
        "- Absence d'un module de paiement pour les inscriptions.\n"
        "- Manque de gestion multi-filiales.\n\n"
        "Ameliorations futures :\n"
        "- Ajout d'une application mobile hybride utilisant les API REST.\n"
        "- Integration d'un module de visioconference pour les cours a distance.\n"
        "- Mise en place d'analyse predictive sur les performances des etudiants (IA)."
    )
    pdf.chapter_body(ccl_txt)
    
    pdf.output("Rapport_Mini_Projet_Spring_Boot.pdf")
    print("PDF genere avec succes.")

# Copy images
img_mapping = {
    'use_case_diagram.png': 'C:/Users/ranim/.gemini/antigravity/brain/04549280-432c-4164-9bf0-b1398731b7c8/use_case_diagram_1769024844946.png',
    'class_diagram.png': 'C:/Users/ranim/.gemini/antigravity/brain/04549280-432c-4164-9bf0-b1398731b7c8/class_diagram_1769024888139.png',
    'sequence_diagram_enrollment.png': 'C:/Users/ranim/.gemini/antigravity/brain/04549280-432c-4164-9bf0-b1398731b7c8/sequence_diagram_enrollment_1769024916226.png',
    'component_diagram_ssr_csr.png': 'C:/Users/ranim/.gemini/antigravity/brain/04549280-432c-4164-9bf0-b1398731b7c8/component_diagram_ssr_csr_1769024952310.png',
    'dashboard_mockup.png': 'C:/Users/ranim/.gemini/antigravity/brain/04549280-432c-4164-9bf0-b1398731b7c8/dashboard_mockup_1769024977714.png'
}

for local_name, source_path in img_mapping.items():
    if os.path.exists(source_path):
        shutil.copy(source_path, local_name)

generate_pdf()
