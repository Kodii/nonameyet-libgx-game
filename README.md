# nonameyet-libgx-game


### 1. GitHub commit pattern: 

    1. capital letter
       Accelerate to 88 miles per hour
       Instead of:
       accelerate to 88 miles per hour 

    2. period
       Open the pod bay doors
       Instead of:
       Open the pod bay doors.
       
    3. complete the following sentence:
        `If applied, this commit will` your subject line here
        example:
            If applied, this commit will refactor subsystem X for readability
            If applied, this commit will update getting started documentation
            
    4. body to explain what and why vs. how
    
    full example:
   
    `Simplify serialize.h's exception handling
    
       Remove the 'state' and 'exceptmask' from serialize.h's stream
       implementations, as well as related methods.
    
       As exceptmask always included 'failbit', and setstate was always
       called with bits = failbit, all it did was immediately raise an
       exception. Get rid of those variables, and replace the setstate
       with direct exception throwing (which also removes some dead
       code).
    
       As a result, good() is never reached after a failure (there are
       only 2 calls, one of which is in tests), and can just be replaced
       by !eof().
    
       fail(), clear(n) and exceptions() are just never called. Delete
       them.`
       
#### 2. resources

    1. https://www.youtube.com/watch?v=a8MPxzkwBwo&list=PLZm85UZQLd2SXQzsF-a0-pPF6IWDDdrXt&index=1
        super mario
    
    2. https://www.youtube.com/watch?v=0vOTrSD5lDA&list=PLTKHCDn5RKK-seXZveiSQuSXkLq3wBYn1&index=1
        creating 2d game using box2d and ashley
        
    3. http://file.allitebooks.com/20160104/Mastering%20LibGDX%20Game%20Development.pdf
        pdf Mastering LibGDX Game Development
        
    4. https://www.youtube.com/watch?v=p024Ix0I8W0
        2d lights     
        
#### 3. helpers
    
    1. Gtx Texture Packer - A simple way to pack and manage texture atlases for LibGDX game framework.
    https://github.com/crashinvaders/gdx-texture-packer-gui
    
    2. Tiled - level editor
    https://www.mapeditor.org/