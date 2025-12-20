# PROJECT B - EXACT CODE FIXES

Complete code snippets ready to implement for chess application.

---

## FIX #1: Add CORS to UserController

**File**: `chess/backend/src/main/java/com/chess/backend/controller/UserController.java`

**Current Code**:
```java
package com.chess.backend.controller;

import com.chess.backend.model.dto.LoginResponse;
import com.chess.backend.model.entity.User;
import com.chess.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
```

**Replace With**:
```java
package com.chess.backend.controller;

import com.chess.backend.model.dto.LoginResponse;
import com.chess.backend.model.entity.User;
import com.chess.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", 
             allowCredentials = "true",
             maxAge = 3600)
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
```

**Add Import**:
```java
import org.springframework.web.bind.annotation.CrossOrigin;
```

---

## FIX #2: Add CORS to GameController

**File**: `chess/backend/src/main/java/com/chess/backend/controller/GameController.java`

**Current Code**:
```java
package com.chess.backend.controller;

import com.chess.backend.model.entity.Game;
import com.chess.backend.model.entity.Move;
import com.chess.backend.model.entity.User;
import com.chess.backend.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
```

**Replace With**:
```java
package com.chess.backend.controller;

import com.chess.backend.model.entity.Game;
import com.chess.backend.model.entity.Move;
import com.chess.backend.model.entity.User;
import com.chess.backend.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200",
             allowCredentials = "true",
             maxAge = 3600)
@RestController
@RequestMapping("/games")
public class GameController {
```

**Add Import**:
```java
import org.springframework.web.bind.annotation.CrossOrigin;
```

---

## FIX #3: Add CORS to MoveController

**File**: `chess/backend/src/main/java/com/chess/backend/controller/MoveController.java`

**Current Code**:
```java
@RestController
@RequestMapping("/moves")
public class MoveController {
```

**Replace With**:
```java
@CrossOrigin(origins = "http://localhost:4200",
             allowCredentials = "true",
             maxAge = 3600)
@RestController
@RequestMapping("/moves")
public class MoveController {
```

---

## FIX #4: Implement Proper Auth Guard

**File**: `chess/frontend/src/app/auth/auth.guard.ts`

**Current Code**:
```typescript
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard = () => {
    return true;
};
```

**Replace With**:
```typescript
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard = () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // ✅ Get token from storage
    const token = authService.getToken();
    
    // ✅ Check if token exists and is not the default guest token
    if (token && token !== 'guest-token') {
        return true;  // Allow access - user is authenticated
    }
    
    // ❌ Not authenticated - redirect to login
    console.warn('Access denied: User not authenticated. Redirecting to login...');
    router.navigate(['/login']);
    return false;
};
```

---

## FIX #5: Create HTTP Interceptor for JWT

**File**: `chess/frontend/src/app/auth/auth.interceptor.ts` (NEW FILE - CREATE IT)

```typescript
import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  
  // ✅ Get JWT token from storage
  const token = authService.getToken();

  // ✅ Only add Authorization header if token exists and is not guest token
  if (token && token !== 'guest-token') {
    // Clone request and add Authorization header
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('Auth header added to request:', req.url);
  }
  
  // Pass request through to next handler
  return next(req);
};
```

---

## FIX #6: Update App Config with Interceptor

**File**: `chess/frontend/src/app/app.config.ts`

**Current Code**:
```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // provideClientHydration(), // Disabled to prevent SSR issues with localStorage
    provideHttpClient(withFetch())
  ]
};
```

**Replace With**:
```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // provideClientHydration(), // Disabled to prevent SSR issues with localStorage
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor])  // ✅ Add interceptor to attach JWT
    )
  ]
};
```

---

## FIX #7: Add Backend Authorization to GameController

**File**: `chess/backend/src/main/java/com/chess/backend/controller/GameController.java`

**Current Code**:
```java
@PostMapping("/move")
public ResponseEntity<Move> move(@RequestBody Move move) {
    Move savedMove = gameService.saveMove(move);
    messagingTemplate.convertAndSend("/topic/moves/" + move.getGame().getId(), savedMove);
    return ResponseEntity.ok(savedMove);
}

@PostMapping("/create")
public ResponseEntity<Game> create(@RequestParam Long player1Id, @RequestParam Long player2Id) {
    Game game = gameService.createGame(
            new User() {{
                setId(player1Id);
            }},
            new User() {{
                setId(player2Id);
            }});
    messagingTemplate.convertAndSend("/topic/game-start/" + player1Id, game.getId());
    messagingTemplate.convertAndSend("/topic/game-start/" + player2Id, game.getId());

    return ResponseEntity.ok(game);
}
```

**Replace With**:
```java
@PostMapping("/move")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<Move> move(@RequestBody Move move, Authentication authentication) {
    try {
        // ✅ Get authenticated user
        User currentUser = (User) authentication.getPrincipal();
        
        // ✅ Verify user is a player in this game
        Game game = move.getGame();
        if (!game.getPlayer1().getId().equals(currentUser.getId()) && 
            !game.getPlayer2().getId().equals(currentUser.getId())) {
            System.err.println("Authorization failed: User " + currentUser.getId() + 
                             " is not a player in game " + game.getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // ✅ Save move and notify players
        Move savedMove = gameService.saveMove(move);
        messagingTemplate.convertAndSend("/topic/moves/" + move.getGame().getId(), savedMove);
        System.out.println("Move saved and sent to players: " + savedMove.getId());
        return ResponseEntity.ok(savedMove);
    } catch (Exception e) {
        System.err.println("Error saving move: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/create")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<Game> create(
        @RequestParam Long player1Id, 
        @RequestParam Long player2Id,
        Authentication authentication) {
    try {
        // ✅ Get authenticated user
        User currentUser = (User) authentication.getPrincipal();
        
        // ✅ Verify requester is player1 (initiator)
        if (!currentUser.getId().equals(player1Id)) {
            System.err.println("Authorization failed: User " + currentUser.getId() + 
                             " cannot create game for player " + player1Id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // ✅ Create game with authorized players
        Game game = gameService.createGame(
                new User() {{
                    setId(player1Id);
                }},
                new User() {{
                    setId(player2Id);
                }});
        
        // ✅ Notify both players
        messagingTemplate.convertAndSend("/topic/game-start/" + player1Id, game.getId());
        messagingTemplate.convertAndSend("/topic/game-start/" + player2Id, game.getId());
        System.out.println("Game created: " + game.getId() + " between " + player1Id + " and " + player2Id);

        return ResponseEntity.ok(game);
    } catch (Exception e) {
        System.err.println("Error creating game: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

**Add Imports**:
```java
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
```

---

## FIX #8: Add Backend Authorization to UserController (Optional but Recommended)

**File**: `chess/backend/src/main/java/com/chess/backend/controller/UserController.java`

**Update GetOnlineUsers**:
```java
@GetMapping("/online")
@PreAuthorize("isAuthenticated()")
public java.util.List<User> getOnlineUsers(Authentication authentication) {
    // ✅ Only authenticated users can see online players
    User currentUser = (User) authentication.getPrincipal();
    System.out.println("User " + currentUser.getUsername() + " fetching online users");
    return userService.getOnlineUsers();
}
```

---

## SUMMARY OF CHANGES

| File | Change | Type | Priority |
|------|--------|------|----------|
| UserController.java | Add @CrossOrigin | Backend | CRITICAL |
| GameController.java | Add @CrossOrigin | Backend | CRITICAL |
| MoveController.java | Add @CrossOrigin | Backend | CRITICAL |
| auth.guard.ts | Implement token validation | Frontend | CRITICAL |
| auth.interceptor.ts | Create new interceptor | Frontend | HIGH |
| app.config.ts | Add interceptor to providers | Frontend | HIGH |
| GameController.java | Add @PreAuthorize & auth checks | Backend | HIGH |
| UserController.java | Add @PreAuthorize to getOnlineUsers | Backend | MEDIUM |

---

## TESTING AFTER FIXES

### Test 1: CORS Working
```bash
# Terminal 1: Start backend
cd chess/backend
./mvnw spring-boot:run

# Terminal 2: Start frontend  
cd chess/frontend
ng serve

# Browser: Check console (F12) for CORS errors - should be NONE
```

### Test 2: Auth Guard Working
```typescript
// In browser console:
localStorage.removeItem('token');
// Navigate to http://localhost:4200/players
// Should redirect to /login

localStorage.setItem('token', 'test-token-123');
// Navigate to http://localhost:4200/players
// Should load PlayersListComponent
```

### Test 3: JWT Interceptor Working
```typescript
// In browser Network tab (F12):
// 1. Register and login
// 2. Click "Online Users" or make any API call
// 3. Check Request Headers → Authorization: Bearer <token>
// Should be present
```

### Test 4: Authorization Working
```typescript
// Backend logs should show:
// "Authorization failed: User X is not a player in game Y"
// When trying to make moves in games you're not part of
```

---

## FILE CREATION CHECKLIST

```
☐ UserController.java → Add @CrossOrigin
☐ GameController.java → Add @CrossOrigin  
☐ MoveController.java → Add @CrossOrigin
☐ auth.guard.ts → Replace with full implementation
☐ auth.interceptor.ts → Create NEW file
☐ app.config.ts → Update to include interceptor
☐ GameController.java → Add @PreAuthorize and auth checks
☐ Restart backend: mvn spring-boot:run
☐ Restart frontend: ng serve
☐ Test in browser
☐ Check Network tab for Authorization headers
☐ Check backend logs for authentication messages
```

---

## VALIDATION COMMANDS

```bash
# Check if backend started successfully
curl http://localhost:8081/user/online
# Should return 200 with user list (or empty if no users)

# Check if frontend can reach backend (after login)
curl -H "Authorization: Bearer test-token" http://localhost:8081/games

# In browser console - should see interceptor logs:
// "Auth header added to request: http://localhost:8081/user/login"
```

---

**All fixes are ready to implement. Apply them in order for best results.**

