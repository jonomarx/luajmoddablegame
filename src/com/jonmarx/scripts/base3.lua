jframe = luajava.newInstance("javax.swing.JFrame", table:getValue("frameTitle"))
jframe:setSize(table:getValue("frameWidth"), table:getValue("frameHeight"))
jframe:setDefaultCloseOperation(table:getValue("frameCloseOperation"))
jframe:setResizable(table:getValue("frameResizable"))

gameImpl = luajava.createProxy("com.jonmarx.base.GameImpl", 
    {
        run = function()
            
        end,

        paint = function(g)
            
        end,

        update = function()
            if tick == nil then
                tick = 0
            end
            tick = tick + 1
        end,
    })
game = luajava.newInstance("com.jonmarx.base.Game", gameImpl)
table:setValue("game", game)

eventImpl = luajava.createProxy("com.jonmarx.base.EventHandlerImpl",
    {
        addEventCondition = function(condition, event) -- learn metatables
            if thisTable == nil then thisTable = {} end
            if thisTable[condition] == nil then
                thisTable[condition] = {}
            end
            if(event ~= nil) then
                thisTable[condition][thistable# + 1] = event
            end
        end,
        removeEventCondition = function(event)
            for k,v in pairs(thisTable) do
                if v[event] ~= nil then
                    v[event] = nil
                end
            end
        end,
        processEvent = function(condition)
            if thisTable == nil then thisTable = {} end
            local use = thisTable[condition]
            if use == nil then use = {} end

            for k,v in pairs(use) do
                if k == nil then goto continue end
                if type(v) == "string" then goto continue end
                
                k:run()
                ::continue::
            end
        end,
    })

eventHandler = luajava.newInstance("com.jonmarx.base.EventHandler", eventImpl)
table:setValue("eventHandler", eventHandler)
eventHandler:addEventCondition("tick", luajava.createProxy("java.lang.Runnable", 
    {
        run = function()
            print("HI")
        end
    }))

jframe:setContentPane(game)
jframe:setVisible(true)

function tick60()
    jframe:repaint()
    game:update()
    table:getValue("eventHandler"):processEvent("tick")
end

timer = luajava.newInstance("javax.swing.Timer", 16, luajava.createProxy("java.awt.event.ActionListener", 
    {
        actionPerformed = function(e)
            tick60()
        end
    }))
timer:start()