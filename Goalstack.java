/*
Chaitrali Londhe
421075
*/
package goalstack;

import java.util.*;

public class Goalstack {
         //init:  on(a,b)^on(c,d)^armempty^ontable(b)^ontable(d)^clear(a)^clear(c)
        
        //final:  on(a,b)^on(b,d)
    
    Stack<String> stack = new Stack<>();
    ArrayList<String> predicates = new ArrayList<>();
    ArrayList<String> actions = new ArrayList<>();
    ArrayList<String> plan = new ArrayList<>();
    ArrayList<String> state = new ArrayList<>();
    
    void setup(String initialstate,String finalstate)
    {
        predicates.add("on");       //array of predicates
        predicates.add("clear");
        predicates.add("holding");
        predicates.add("armempty");
        predicates.add("ontable");
        actions.add("stack");      //array of actions
        actions.add("unstack");
        actions.add("pickup");
        actions.add("putdown");
        
        String[] inarr = initialstate.split("\\^");
        String[] fiarr = finalstate.split("\\^");
        
        for(String s:inarr)
        {
            state.add(s);
        }
        
        for(String s:fiarr)
        {
            stack.push(s);
        }
    }
    
    String findstateEle(String st,String y) //for on unstack
    {
        String starr[];
        for(String s:state)
        {
                if(s.startsWith(st+"("))
                {
                        starr = s.split("\\(|,|\\)");
                        if(starr[2].equals(y))
                        {
                                return starr[1];
                        }
                }
        }
        return null;
    }
    
    String findHolding() //for holding
    {
        String starr[];
        for(String s:state)
        {
                if(s.startsWith("holding"))
                {
                        starr = s.split("\\(|,|\\)");
                                return starr[1];
                }
        }
        return null;
    }
    
    void stackActionNPrecon(String check,String x,String y)
    {
        if(check.equals("on"))
        { 
                stack.push("stack("+x+","+y+")"); //action
                
                stack.push("holding("+x+")"); //preconditions
                stack.push("clear("+y+")");
        }
        else if(check.equals("clear"))
        { 
                String a = findstateEle("on",x); //find first value
                stack.push("unstack("+a+","+x+")"); //action
                
                stack.push("on("+a+","+x+")"); //preconditions
                stack.push("armempty");
                stack.push("clear("+a+")");                
        }
        else if(check.equals("holding"))
        { 
                if(state.contains("ontable("+x+")"))
                {
                    stack.push("pickup("+x+")"); //action
                    
                    stack.push("armempty"); //sequence of pushing onto stack matters
                    stack.push("clear("+x+")"); //preconditions
                    stack.push("ontable("+x+")");
                }
                else
                {
                    String a = findstateEle("on",x); //find first value
                    stack.push("unstack("+a+","+x+")"); //action

                    stack.push("on("+a+","+x+")"); //preconditions
                    stack.push("armempty");
                    stack.push("clear("+a+")"); 
                }
        }
        else if(check.equals("armempty") || check.equals("ontable"))
        { 
            String a = findHolding();
                stack.push("putdown("+a+")"); //action
                
                stack.push("holding("+a+")"); //precondition
        }
        
/*        System.out.println();
        System.out.println("STACK:");
       for(String s:stack)
        {
            System.out.println(s);
        }*/
    }

    void method()
    {
        String top,action=null,precon,check[];
        while(!stack.empty())
        {
            top = stack.peek();
            //System.out.println("TOP:  "+top);
            check = top.split("\\(|,|\\)");
            if(predicates.contains(check[0]))
            {               
                if(!state.contains(top))
                { 
                    //find action for that predicate and preconditions for that action and then push onto stack
                    switch (check.length) {
                        case 3: 
                            stackActionNPrecon(check[0],check[1],check[2]);
                            break;
                        case 2:
                            stackActionNPrecon(check[0],check[1],"");
                            break;                            
                        case 1:
                            stackActionNPrecon(check[0],"","");
                            break;
                    }
                }
                else
                {
                    stack.pop();
                }
            }
            else if(actions.contains(check[0]))
            {
                String val;
                plan.add(top);
                if(check[0].equals("unstack"))
                {
                  val = "holding("+check[1]+")";
                  if(!state.contains(val))  state.add(val);
                  val = "clear("+check[2]+")";
                  if(!state.contains(val))  state.add(val);
                  
                  state.remove("armempty");
                  state.remove("clear("+check[1]+")");
                  state.remove("on("+check[1]+","+check[2]+")");                 
                }
                else if(check[0].equals("pickup"))
                {
                  val = "holding("+check[1]+")";
                  if(!state.contains(val))  state.add(val);
                  
                  state.remove("ontable("+check[0]+")");
                  state.remove("clear("+check[1]+")");
                  stack.remove("armempty");
                }
                else if(check[0].equals("stack"))
                {
                    if(!state.contains("armempty")) state.add("armempty");
                    val = "on("+check[1]+","+check[2]+")";
                    if(!state.contains(val))  state.add(val);
                    val = "clear("+check[1]+")";
                    if(!state.contains(val))  state.add(val);
                    
                    state.remove("holding("+check[1]+")");
                    state.remove("clear("+check[2]+")");
                }
                else if(check[0].equals("putdown"))
                {
                    if(!state.contains("armempty")) state.add("armempty");
                    val = "ontable("+check[1]+")";
                    if(!state.contains(val))  state.add(val);
                    val = "clear("+check[1]+")";
                    if(!state.contains(val))  state.add(val);      
                    
                    state.remove("holding("+check[1]+")");
                }
                stack.pop();
        
/*        System.out.println();
        System.out.println("STATE:");
        for(String s: state)
        {
            System.out.println(s);
        }*/
            }
        }
        
        System.out.println();
        System.out.println("PLAN:");
        for(String s: plan)
        {
            System.out.println(s);
        }
    }
    
    public static void main(String[] args) {
        String i,f;
        Scanner sc = new Scanner(System.in);
        Goalstack gs = new Goalstack();
        System.out.println("Enter initial state: ");
        i = sc.next();
        System.out.println("Enter final state: ");
        f = sc.next();
        gs.setup(i,f);
        gs.method();
    }    
}

/* OUTPUT:
Enter initial state: 
on(a,b)^on(c,d)^armempty^ontable(b)^ontable(d)^clear(a)^clear(c)
Enter final state: 
on(a,b)^on(b,d)

PLAN:
unstack(c,d)
putdown(c)
unstack(a,b)
putdown(a)
pickup(b)
stack(b,d)
pickup(a)
stack(a,b)
*/
