import util 
import numpy as np 
import ast

class DecisionTree(object):
    def __init__(self, max_depth):
        # Initializing the tree as an empty dictionary or list, as preferred
        self.tree = {}
        self.max_depth = max_depth
        pass
    	
    
    def learn(self, X, y, par_node = {}, depth=0):
        # TODO: Train the decision tree (self.tree) using the the sample X and labels y
        # You will have to make use of the functions in utils.py to train the tree

        # Use the function best_split in util.py to get the best split and 
        # data corresponding to left and right child nodes
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)
        ### Implement your code here
        #############################################
        
        
        if (self.isLeaf(y)) or depth == self.max_depth:
            # if not self.isLeaf(y):
            #     print("not a leaf")
            self.tree = self.classification(y)
            return self.classification(y)
        else:
            # print("depth: {}".format(depth))
            depth +=1
            split_attribute, split_value, X_left, X_right, y_left, y_right = util.best_split(X, y)
            
             
            key = "{} {}".format(split_attribute, split_value)
            tree = {key: []}
            # if (len(y_left) < 1):
                
            #     print("y_left has a problem")
            #     print(X_left)
            #     print(y_left)
                
            # elif (len(y_right) < 1):
            #     print("y_right has a problem")
            #     print(X_right)
            #     print(y_right)
                
            left = self.learn(X_left, y_left, {}, depth)
            right = self.learn(X_right, y_right, {}, depth)
            
            if left == right:
                tree = left
            
            else:
                tree[key].append(left)
                tree[key].append(right)
                
            # result = "Tree at level {}: {}".format(depth, tree)
            # print (result)
            # if depth == 1:
            #     print (self.tree)
            self.tree = tree  
            return tree
        
        
        # self.tree = tree
                
            
            
        #############################################


    def classify(self, record):
        # TODO: classify the record using self.tree and return the predicted label
        ### Implement your code here
        #############################################
        
        tree = self.tree
        
        if not isinstance (tree, dict):
            return tree
        
        while (True):
            
            key = list(tree.keys())[0]
            variable, value = key.split()
            variable = int(variable)
            value = float(variable)
            
            if record[variable] <= value:
                result = tree[key][0]
            else:
                result = tree[key][1]
                
            if not isinstance(result, dict):
                return result 
            else:
                tree = result
            
        #############################################
        
    def isLeaf(self, input):
        values = np.unique(input)
        if len(values) ==1:
            return True
        else:
            return False

    def classification (self, input):
        classes, counts = np.unique(input, return_counts = True)
        index = counts.argmax()
        return classes[index]
    
        
        