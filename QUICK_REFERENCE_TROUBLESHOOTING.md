# Spring Validation Project - Quick Reference & Troubleshooting Guide

## 🚀 QUICK START GUIDE

### Starting the Backend (Spring Boot)
```bash
cd c:\spring-validation\backend\backend

# Option 1: Run JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Option 2: Run Maven
./mvnw spring-boot:run

# Option 3: IDE
- Right-click BackendApplication.java → Run
```
**Expected Output**: Server running on port 8081
**Health Check**: http://localhost:8081/api/etudiants → Returns JSON array

---

### Starting the Frontend (Angular)
```bash
cd c:\spring-validation\frontend

# Run development server
npm start
# or
ng serve

# Build for production
npm run build
```
**Expected Output**: Angular compiled successfully
**Access**: http://localhost:4200
**Auto-reload**: Any file changes automatically refresh browser

---

## 📋 ROUTING QUICK REFERENCE

### Frontend Routes
| URL | Component | Purpose |
|-----|-----------|---------|
| `/` | - | Redirects to `/etudiants` |
| `/etudiants` | ListEtudiantComponent | View all students |
| `/etudiants/create` | CreateEtudiantComponent | Create new student |
| `/etudiants/edit/:matricule` | EditEtudiantComponent | Edit existing student |

### Backend Routes
| Method | URL | Response |
|--------|-----|----------|
| GET | `/api/etudiants` | List of all students |
| POST | `/api/etudiants` | Create new student |
| PUT | `/api/etudiants/{matricule}` | Update student |
| DELETE | `/api/etudiants/{matricule}` | Delete student |

---

## 🔗 API ENDPOINTS REFERENCE

### 1. Get All Students
```bash
curl http://localhost:8081/api/etudiants
```

**Response (200 OK)**:
```json
[
  {
    "matricule": "MAT001",
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean@example.com",
    "dateInscription": "2024-01-15"
  }
]
```

---

### 2. Create Student
```bash
curl -X POST http://localhost:8081/api/etudiants \
  -H "Content-Type: application/json" \
  -d '{
    "matricule": "MAT002",
    "nom": "Martin",
    "prenom": "Marie",
    "email": "marie@example.com"
  }'
```

**Response (201 CREATED)**:
```json
{
  "matricule": "MAT002",
  "nom": "Martin",
  "prenom": "Marie",
  "email": "marie@example.com",
  "dateInscription": "2024-12-16"
}
```

---

### 3. Update Student
```bash
curl -X PUT http://localhost:8081/api/etudiants/MAT001 \
  -H "Content-Type: application/json" \
  -d '{
    "matricule": "MAT001",
    "nom": "Dupont",
    "prenom": "Jean-Paul",
    "email": "jeanpaul@example.com"
  }'
```

**Response (200 OK)**:
```json
{
  "matricule": "MAT001",
  "nom": "Dupont",
  "prenom": "Jean-Paul",
  "email": "jeanpaul@example.com",
  "dateInscription": "2024-01-15"
}
```

---

### 4. Delete Student
```bash
curl -X DELETE http://localhost:8081/api/etudiants/MAT001
```

**Response (204 NO CONTENT)**: No body

---

## ⚠️ TROUBLESHOOTING GUIDE

### Problem: "Frontend can't reach backend" (ERR_CONNECTION_REFUSED)

**Symptoms**:
- Browser shows CORS errors
- Network tab shows failed requests to localhost:8081
- Console: `GET http://localhost:8081/api/etudiants net::ERR_CONNECTION_REFUSED`

**Solutions**:
1. **Check backend is running**
   ```bash
   # Test if backend is listening
   netstat -ano | findstr :8081
   # or curl
   curl http://localhost:8081/api/etudiants
   ```

2. **Verify backend started successfully**
   - Look for "Started BackendApplication" in console
   - Check for database connection errors

3. **Check port 8081 not in use**
   ```bash
   netstat -ano | findstr :8081
   # Kill if needed: taskkill /PID <PID> /F
   ```

---

### Problem: CORS Error in Browser

**Symptoms**:
```
Access to XMLHttpRequest at 'http://localhost:8081/api/etudiants' 
from origin 'http://localhost:4200' has been blocked by CORS policy
```

**Solutions**:
1. **Verify @CrossOrigin annotation**
   ```java
   // Should be present in EtudiantController
   @CrossOrigin(origins = "http://localhost:4200")
   ```

2. **Check frontend URL matches**
   - Frontend must be on: `http://localhost:4200`
   - Not: `http://127.0.0.1:4200` (different origin!)

3. **Backend not started**
   - CORS is only relevant if backend responds
   - First check backend is actually running

---

### Problem: Route Not Found (404 in Angular)

**Symptoms**:
- URL changes but component doesn't load
- Blank page after navigation
- Console shows no errors

**Solutions**:
1. **Check route is defined in app.routes.ts**
   ```typescript
   export const routes: Routes = [
     { path: '', redirectTo: 'etudiants', pathMatch: 'full' },
     { path: 'etudiants', component: ListEtudiantComponent },
     { path: 'etudiants/create', component: CreateEtudiantComponent },
     { path: 'etudiants/edit/:matricule', component: EditEtudiantComponent }
   ];
   ```

2. **Check component is imported in route**
   ```typescript
   import { ListEtudiantComponent } from './etudiants/list-etudiant/list-etudiant.component';
   ```

3. **Route order matters!**
   - Specific routes (`/etudiants/create`) before parameterized (`/etudiants/edit/:matricule`)

4. **Check router outlet exists**
   ```html
   <!-- In app.component.html -->
   <router-outlet></router-outlet>
   ```

---

### Problem: Form Validation Fails but No Error Message

**Symptoms**:
- Click submit button, nothing happens
- No error visible in UI
- Console shows form is invalid

**Solutions**:
1. **Check validation error template**
   ```html
   <div *ngIf="form.get('email')?.touched && form.get('email')?.invalid">
     Email invalide
   </div>
   ```

2. **Mark touched on submit**
   ```typescript
   } else {
     this.form.markAllAsTouched();
   }
   ```

3. **Check browser console** for subscription errors
   ```typescript
   error: err => console.error('Error:', err)
   ```

---

### Problem: "Database connection refused"

**Symptoms**:
- Backend starts but dies immediately
- Log: "Could not connect to mysql://localhost:3306/mydb"
- Status: 500 Internal Server Error

**Solutions**:
1. **Check MySQL is running**
   ```bash
   # Windows Service
   services.msc → Look for MySQL service
   # or
   mysql -u test -p test123 -e "SELECT 1"
   ```

2. **Verify credentials in application.yaml**
   ```yaml
   datasource:
     url: jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true
     username: test
     password: test123
   ```

3. **Create database if missing**
   ```sql
   CREATE DATABASE mydb;
   ```

4. **Check Liquibase runs**
   - Look for migration logs in console
   - Should see: "Successfully acquired change log lock"

---

### Problem: Student Created but Not Visible in List

**Symptoms**:
- POST request succeeds (201)
- But GET doesn't show new student
- No database error

**Solutions**:
1. **Check database commit**
   - Service has @Transactional (implicit)
   - Spring auto-commits on method exit

2. **Verify database persistence**
   ```sql
   SELECT * FROM etudiant;
   ```

3. **Check component refresh**
   ```typescript
   // Should navigate to list which calls getAll()
   this.router.navigate(['/etudiants']);
   ```

4. **Manual refresh browser** if navigation doesn't trigger reload

---

### Problem: Edit Form Doesn't Load Student Data

**Symptoms**:
- Navigate to `/etudiants/edit/MAT001`
- Form is empty
- No console errors

**Solutions**:
1. **Check route parameter extraction**
   ```typescript
   const m = this.route.snapshot.paramMap.get('matricule');
   if (!m) {
     alert('Matricule manquant');
     return;
   }
   ```

2. **Verify getAll() is called**
   - Current implementation loads ALL students
   - Then searches for matching matricule
   - Check browser Network tab → XHR for GET request

3. **Check form patching**
   ```typescript
   this.form.patchValue(e); // Should populate form fields
   ```

---

## 🔧 DEVELOPMENT TIPS

### Debug Network Requests
```javascript
// Chrome DevTools → Network tab
// Filter: Fetch/XHR
// Click request to see:
// - Request Headers
// - Request Body (for POST/PUT)
// - Response Headers
// - Response Body
```

### Debug Angular Routes
```typescript
// app.routes.ts - add trace
import { provideRouter } from '@angular/router';
import { enableDebugTools } from '@angular/platform-browser';

export const routes: Routes = [
  // Enable debug logging
  { enableTracing: true } // (uncomment in appConfig)
];

// Console output: Navigation Start, Resolved Component, etc.
```

### Debug Backend Requests
```java
// Enable Spring request logging in application.yaml
logging:
  level:
    org.springframework.web: DEBUG
    org.springframework.security: DEBUG
    
// Or individual endpoints
@RequestMapping(value = "/api/etudiants", produces = "application/json")
```

### Test Endpoints with Postman
1. Create collection "Spring Validation"
2. Import requests from guide above
3. Save variables:
   - `{{baseUrl}}` = http://localhost:8081/api/etudiants
   - `{{matricule}}` = MAT001

---

## 📊 DATABASE QUERIES

### View All Students
```sql
SELECT * FROM etudiant;
```

### Find Student by Matricule
```sql
SELECT * FROM etudiant WHERE matricule = 'MAT001';
```

### Find by Email
```sql
SELECT * FROM etudiant WHERE email = 'jean@example.com';
```

### Count Total Students
```sql
SELECT COUNT(*) FROM etudiant;
```

### Delete All Students (⚠️ Careful!)
```sql
DELETE FROM etudiant;
```

### Check Table Schema
```sql
DESCRIBE etudiant;
```

---

## 🔐 SECURITY CONSIDERATIONS

### Current Setup (Development)
- ⚠️ CORS allows all methods from localhost:4200
- ⚠️ No authentication/authorization
- ⚠️ Database credentials in plain text
- ⚠️ No HTTPS

### Recommended for Production
1. **Add Security**
   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   ```

2. **Use Environment Variables**
   ```yaml
   datasource:
     username: ${DB_USER}
     password: ${DB_PASSWORD}
   ```

3. **Enable HTTPS**
   ```yaml
   server:
     ssl:
       key-store: keystore.jks
       key-store-password: ${KEY_STORE_PASSWORD}
   ```

4. **Restrict CORS**
   ```java
   @CrossOrigin(origins = "https://myapp.com", 
                allowCredentials = "true",
                maxAge = 3600)
   ```

---

## 📚 FILE STRUCTURE FOR REFERENCE

```
spring-validation/
├── backend/
│   └── backend/
│       ├── src/main/
│       │   ├── java/com/gestion/backend/
│       │   │   ├── BackendApplication.java (Entry Point)
│       │   │   ├── controllers/
│       │   │   │   └── EtudiantController.java (Routes)
│       │   │   ├── services/
│       │   │   │   ├── EtudiantService.java (Interface)
│       │   │   │   └── impl/
│       │   │   │       └── EtudiantServiceImpl.java (Logic)
│       │   │   ├── repositories/
│       │   │   │   └── EtudiantRepository.java (Database)
│       │   │   ├── entities/
│       │   │   │   └── Etudiant.java (JPA Entity)
│       │   │   └── dtos/
│       │   │       └── EtudiantDto.java (Response)
│       │   ├── resources/
│       │   │   ├── application.yaml (Config)
│       │   │   └── db/changelog/ (Migrations)
│       │   └── inputs/
│       │       └── EtudiantInput.java (Request)
│       └── pom.xml (Dependencies)
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.component.* (Root)
│   │   │   ├── app.routes.ts (Routing)
│   │   │   ├── app.config.ts (Config)
│   │   │   ├── models/
│   │   │   │   └── etudiant.model.ts (Interface)
│   │   │   └── etudiants/
│   │   │       ├── etudiant.service.ts (API Service)
│   │   │       ├── list-etudiant/
│   │   │       ├── create-etudiant/
│   │   │       └── update-etudiant/
│   │   └── main.ts (Bootstrap)
│   ├── package.json (Dependencies)
│   └── angular.json (Config)
│
├── VALIDATION_PROJECT_ANALYSIS.md (Architecture)
├── ROUTING_AND_REQUEST_FLOW_DIAGRAM.md (Visual Guide)
└── QUICK_REFERENCE_TROUBLESHOOTING.md (This File)
```

---

## ✅ VERIFICATION STEPS

Run this checklist to ensure everything works:

```
□ Backend compiles:     mvn clean package -DskipTests
□ Backend starts:       java -jar target/backend-0.0.1-SNAPSHOT.jar
□ Check port 8081:      netstat -ano | findstr :8081
□ Test API:             curl http://localhost:8081/api/etudiants
□ Frontend installs:    npm install
□ Frontend builds:      ng build
□ Frontend starts:      ng serve
□ Check port 4200:      netstat -ano | findstr :4200
□ Browse app:           http://localhost:4200
□ Create student:       Fill form, click Créer
□ Update student:       Click Modifier, make changes
□ Delete student:       Click Supprimer
□ Check DB:             mysql -u test -p test123 mydb
□ Query table:          SELECT * FROM etudiant;
```

---

## 🎯 PROJECT STATUS: ✅ FULLY FUNCTIONAL

All routing, request flow, and backend integration is working correctly.

**Next Steps**:
- Deploy to staging environment
- Add integration tests
- Implement user authentication
- Add input sanitization
- Setup continuous deployment

